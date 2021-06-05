package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.CategoryRepository;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.xtra.IProductService;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsLookupParser;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookupParser;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;
import com.google.cloud.translate.Translation;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import static com.badals.shop.service.util.AccessUtil.opt;

@Service
public class Pas5Service implements IProductService {

    public static final long DEFAULT_WINDOW = 14400;
    private final Logger log = LoggerFactory.getLogger(Pas5Service.class);
    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final ProductOverrideRepository productOverrideRepository;
    private final PasLookup pasLookup;
    private final MwsLookup mwsLookup;
    private final RedisPasRepository redisPasRepository;
    private final ProductMapper productMapper;
    private final PasItemMapper pasItemMapper;
    private final Translate translateService;


    public Pas5Service(ProductRepository productRepo, CategoryRepository categoryRepository, MerchantRepository merchantRepository, ProductOverrideRepository productOverrideRepository, @Qualifier("us") PasLookup pasLookup, MwsLookup mwsLookup, RedisPasRepository redisPasRepository, ProductMapper productMapper, PasItemMapper pasItemMapper, Translate translateService) {
        this.productRepo = productRepo;
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.productOverrideRepository = productOverrideRepository;
        this.pasLookup = pasLookup;
        this.mwsLookup = mwsLookup;
        this.redisPasRepository = redisPasRepository;
        this.productMapper = productMapper;
        this.pasItemMapper = pasItemMapper;
        this.translateService = translateService;
    }

    public Boolean existsBySku(String sku) {
        return productRepo.existsBySku(sku);
    }

    public Product findBySku(String sku) {
        return productRepo.findOneBySkuAndMerchantId(sku, 1L).get();
    }

    private List<ProductOverride> findOverrides(String asin, String parent) {
        if (parent == null)
            return productOverrideRepository.findBySku(asin);
        return productOverrideRepository.findBySkuIn(Arrays.asList(new String[]{asin, parent}));
    }

    private boolean setVariationType(Product p, VariationType v) {
        p.setVariationType(v);
        if(v == VariationType.PARENT)
            return true;
        return false;
    }

    @SneakyThrows
    @Transactional
    /**
     * Entry point
     */
    public Product lookup(String asin, boolean isParent, boolean isRedis, boolean isRebuild, boolean forcePas) throws NoOfferException, PricingException {

        // Does Product Exist?
        Product product = productRepo.findBySkuJoinChildren(asin, 1L).orElse(null);

        // Get PAS to check how many variation dimensions are there since MWS missed some variation attributes
        int dimCount = 0;
        GetVariationsResponse variationsResponse = pasLookup.variationLookup(asin, 1);

        // Pas Error Flag
        if(variationsResponse.getErrors() != null){
            String errCode =  variationsResponse.getErrors().get(0).getCode();
            if (errCode.equalsIgnoreCase("noresults")) {
                // This is a SIMPLE item
            }
            else if (errCode.equalsIgnoreCase("ItemNotAccessible")){
                return mwsItemShortCircuit(product, asin, true, 0);
            }

        }
        else {
            //
            VariationSummary summary = variationsResponse.getVariationsResult().getVariationSummary();
            dimCount = summary.getVariationDimensions().size();
            int pageCount = summary.getPageCount();
            final GetVariationsResponse variationsResponseFinal = variationsResponse;
            BigDecimal weight = opt(() -> variationsResponseFinal.getVariationsResult().getItems().get(0).getItemInfo().getProductInfo().getItemDimensions().getWeight().getDisplayValue());
            if (pageCount < 6 && weight != null && weight.doubleValue() > PasUtility.MINWEIGHT) // If 5 pages of dimensions or less continue with pas
                forcePas = true;
        }

        if(!forcePas) {
            try {
                return mwsItemShortCircuit(product, asin, isRebuild, dimCount);
            }
            catch (IncorrectDimensionsException e) {
                // swallow and continue with PAS
                log.info("Iam catching IncorrectDimensionsException");
            }
        }


        //PasItemNode current = pasItemNodeSearchRepository.findById(asin);
        if (product == null) {
            product = new Product();
            isRebuild = true;
        }
        else {
            isParent = product.getVariationType().equals(VariationType.PARENT);
            if (product.getExpires() != null && product.getExpires().isAfter(Instant.now()))
                return product;
        }
        if(isParent) isRebuild = true;

        //if (product != null) // && product.getUpdated())
        List<Product> mws = new ArrayList<>();
        GetItemsResponse response = null;
        PasItemNode item = null;
        Map<String, Item> doc;

        // Read from redis cache
/*        if (isRedis && redisPasRepository.getHashOperations().hasKey("pas", asin)) {
            try {
                item = (PasItemNode) redisPasRepository.getHashOperations().get("pas", asin);
            }
            catch (Exception e) {
                redisPasRepository.getHashOperations().delete("pas", asin);
            }
        }*/
        //if

        List<String> list = new ArrayList();
        list.add(asin);

        if (item == null) {
            response = pasLookup.lookup(list);
            if(response.getErrors() != null && response.getErrors().size() > 0 ) {
                ErrorData error = response.getErrors().get(0);
                if (error.getCode().trim().equalsIgnoreCase("ItemNotAccessible")) {
                    //response = mwsLookup.lookup(asin);
                    return mwsItemShortCircuit(product, asin, true, 0);
                }
            }
            doc = parse_response(response.getItemsResult().getItems());
            item = pasItemMapper.itemToPasItemNode(doc.get(asin));
            //redisPasRepository.getHashOperations().put("pas", asin, item);
        }

        //for (String itemId : list) {
            //Map<String, Item> responseList = parse_response(response.getItemsResult().getItems());

        if(isParent)
            isParent = setVariationType(product, VariationType.PARENT);
        else
            isParent = setVariationType(product, VariationType.SIMPLE);

        if (item.getParentAsin() != null && !item.getParentAsin().equals("asin")) {
            if(!existsBySku(item.getParentAsin()) || isRebuild)
               return lookup(item.getParentAsin(), true, isRedis, isRebuild, true);
            product.setVariationType(VariationType.CHILD);
            product.setParentId(findBySku(item.getParentAsin()).getRef());
        }

        List<ProductOverride> overrides = findOverrides(item.getId(), item.getParentAsin());
        product = initProduct(product, item, isParent, overrides);
        product = productRepo.save(product);
        if(isRebuild) {
            //GetVariationsResponse variationsResponse = pasLookup.variationLookup(asin, 1);
            if (variationsResponse != null && variationsResponse.getVariationsResult() != null) {
                PasLookupParser.parseDimensions(product, variationsResponse);
                int page = 1;
                boolean more = true;
                Set<Product> children = product.getChildren();
                if (children == null)
                    children = new HashSet<>();
                List<Variation> variations = new ArrayList<Variation>();
                while (more) {
                    VariationsResult variationsResult = variationsResponse.getVariationsResult();
                    if (variationsResult == null)
                        break;
                    for (PasItemNode childItem : variationsResult.getItems().stream().map(pasItemMapper::itemToPasItemNode).collect(Collectors.toList())) {
                        String childAsin = childItem.getId();
                        Product child = children.stream().filter(x -> x.getSku().equals(childAsin)).findFirst().orElse(findProduct(childAsin));
                        child = initProduct(child, childItem, false, overrides);

                        PasLookupParser.parseVariationAttributes(child, childItem);
                        variations.add(new Variation(child.getRef(), child.getVariationAttributes()));
                        child.setParent(product);
                        child.setParentId(product.getRef());

                        if(child.getPrice() == null)
                            mws.add(child);

                        if (child.getId() == null)
                            children.add(child);
                        child.setVariationType(VariationType.CHILD);
                    }
                    // log.info("This variation set had {} items", variationsResult.getItems().size());
                    if (variationsResult.getItems().size() < 10)
                        more = false;
                    else {
                        page++;
                        variationsResponse = pasLookup.variationLookup(asin, page);
                    }
                }
                product.setChildren(children);
                product.setVariations(variations);
                isParent = setVariationType(product, VariationType.PARENT);
            }
        }
        if(!isParent && product.getPrice() == null)
            mws.add(product);

        for(Product p: mws) {
            p = priceMws(p, overrides);
        }

        product = productRepo.save(product);
        return product;
    }

    private Product findProduct(String sku) {
        return productRepo.findBySku(sku).orElse(new Product());
    }

    @Transactional
    void reassignChildren(final Product finalParent, PasItemNode parentItem) {
        //productRepo.updateParentAllBySku(finalParent.getRef(), parentItem.getVariations().keySet());
        List<Product> existingChildren = productRepo.findAllBySku(parentItem.getVariations().keySet());
    }

    void skip() {};

    @Transactional
    Product createStubs(Product parent, PasItemNode parentItem ) {
        List<VariationOption> options = new ArrayList<>();
        final List<Variation> variations = new ArrayList<>();
        initProduct(parent, parentItem, true, null);

        parent.setVariationType(VariationType.PARENT);
        if(parent.getId() == null)
            productRepo.saveAndFlush(parent);

        productRepo.updateParentAllBySku(parent.getRef(), parentItem.getVariations().keySet());
        parent = productRepo.findBySkuJoinChildren(parent.getSku(), 1L).orElse(parent);

        Set<Product> children = parent.getChildren();

        for(String sku: parentItem.getVariations().keySet()) {
            List<Attribute> value = parentItem.getVariations().get(sku);
            Product child = children.stream().filter(x -> x.getSku().equals(sku)).findFirst().orElse(initStub(sku, value, parent));
            child.setVariationType(VariationType.CHILD);
            child.setVariationAttributes(value);
            child.setParentId(parent.getRef());
            child.setParent(parent);
            //child.setParentId(finalParent);

            child.setMerchantId(1L);
            if(child.getId() ==  null)
                children.add(child);
        }
        children.forEach((child) -> variations.add(new Variation(child.getRef(), child.getVariationAttributes())));
        //finalParent.setChildren(children);
        parent.setVariations(variations);
        int i = 0;
        for (String option : parentItem.getVariationDimensions()) {
            Set<String> values = new HashSet<>();
            for(Product p : children) {
                values.add(p.getVariationAttributes().get(i).getValue());
            }
            options.add(new VariationOption(option,null,new ArrayList<String>(values)));
            i++;
        }



        return parent;
    }

    private Product createProduct(Product product, PasItemNode item) {
        product = initProduct(product, item, false, null);
        //product.setStub(false);
        if(item.getVariationType() != null)
            product.setVariationType(item.getVariationType());
        return product;
    }

    private PasItemNode callPas(String asin) throws PricingException {
        GetItemsResponse response = null;
        List<String> list = new ArrayList();
        list.add(asin);
        Map<String, Item> doc;
        response = pasLookup.lookup(list);

        //ErrorData error = response.getErrors().get(0);
        //if (error.getCode().trim().equalsIgnoreCase("ItemNotAccessible")) {

        if (response.getErrors() != null && response.getErrors().size() > 0) {
            throw new PricingException("Unable to price this item!");
        }

        doc = parse_response(response.getItemsResult().getItems());
        return pasItemMapper.itemToPasItemNode(doc.get(asin));
    }

    public Product mwsItemShortCircuit(Product product, String asin, boolean isRebuild, Integer dimCount) throws NoOfferException, PricingException, IncorrectDimensionsException {

        List<ProductOverride> overrides = findOverrides(asin, null);
        //Product finalParent = null;
        PasItemNode item = null;
        //if(product == null || product.getStub()) {
            //item = mwsLookup.lookup(asin);
        //}


        boolean isPasLookup = false;
        boolean isMwsLookup = false;
        boolean isReset = true;
/*
        if(product != null && product.getMerchantId() == 11L)
            isReset = true;*/

        if(product != null && product.getWeight() != null)
            isPasLookup = true;

        if(overrides != null && overrides.size() > 0)
            isPasLookup = true;

        if(!isPasLookup) {
            try {
                item = mwsLookup.lookup(asin);
            } catch (Exception e) {
                isPasLookup = true;
                isMwsLookup = false;
            }
        }
/*        if(isPasLookup) {
            try {
                item = callPas(asin);
            }
            catch (PricingException e) {
                if (isMwsLookup)
                    item = mwsLookup.lookup(asin);
                else
                    throw e;
            }
        }*/
            //redisPasRepository.getHashOperations().put("pas", asin, item);

        // Not Exists
        if(product == null) {
            // Create Product
            product = createProduct(new Product(), item);

            // Is Child?
            //if(item.getVariationType() == VariationType.CHILD) {
            if(item.getParentAsin() != null && !item.getParentAsin().equals("asin")) {
                String parentAsin = item.getParentAsin();
                overrides = findOverrides(asin, parentAsin);
                Product parent = productRepo.findBySkuJoinChildren(parentAsin, 1L).orElse(null);

                // Parent exists?
                if(parent == null) {
                    PasItemNode parentItem = mwsLookup.lookup(item.getParentAsin());
                    parent = productRepo.findOneBySkuAndMerchantId(item.getParentAsin(),1L).orElse(createProduct(new Product(), parentItem));
                    if(parentItem != null && parentItem.getVariationDimensions() != null && parentItem.getVariationDimensions().size() < dimCount)
                        throw new IncorrectDimensionsException("MWS missing dimensions. "+dimCount+" expected "+parentItem.getVariationDimensions().size()+ " found.");
                    //TODO: Find all children and assign to it (must exclude them from stub creation)
                    parent = createStubs(parent, parentItem);
                    Product child = parent.getChildren().stream().filter(x -> x.getSku().equals(asin)).findFirst().get();
                    child = createProduct(child, item);
                    parent.addChild(child);
                    productRepo.save(parent);
                }
            }
            else {
                // Assume no parent will be queried directly
                product.setVariationType(VariationType.SIMPLE);
            }
        }
        else {
            //TODO: Check expired
            if(isReset)
                product = createProduct(product, item);
            // Is Child?
            if(item.getParentAsin() != null && !item.getParentAsin().equals("asin")) {
                overrides = findOverrides(asin, item.getParentAsin());
                Product parent = null;
                if(product.getParent() == null) { // Unlikely case
                    PasItemNode parentItem = mwsLookup.lookup(item.getParentAsin());
                    String parentAsin = item.getParentAsin();
                    parent = productRepo.findBySkuJoinChildren(parentAsin, 1L).orElse(createProduct(new Product(), parentItem));


/*                    if(parent == null) {*/
                    //parent = createProduct(new Product(), parentItem);
                    //TODO: Find all children and assign to it (must exclude them from stub creation)
                    parent = createStubs(parent, parentItem);
                    productRepo.save(parent);
                        //productRepo.flush();
/*                    }
                    else {
                        reassignChildren(parent, parentItem);
                        productRepo.save(parent);
                    }*/

                }
                else {
                    parent = product.getParent(); //productRepo.findBySkuJoinChildren(item.getParentAsin()).orElse(null);
                }
                if(product.getStub() != null && product.getStub() || isReset) {
                    Product child = parent.getChildren().stream().filter(x -> x.getSku().equals(asin)).findFirst().get();
                    child = createProduct(child, item);
                    child.setVariationType(VariationType.CHILD);
                    parent.addChild(child);
                    parent.setMerchantId(1L);
/*                    if(isReset) {
                        PasItemNode parentItem = mwsLookup.lookup(item.getParentAsin());
                        createStubs(parent, parentItem);
                    }*/
                }

                productRepo.save(parent);
            }

        }
        Product updated = productRepo.findBySkuJoinChildren(asin, 1L).orElse(product);
        updated.weight(PasUtility.calculateWeight(product.getWeight(), PasLookupParser.getOverride(overrides, OverrideType.WEIGHT)));
        if(updated.getWeight() != null) {
            updated = priceMws(updated, overrides);
            if(updated.getPrice() == null  || !updated.getInStock() ) {
                if(!isPasLookup) {
                    try {
                        item = callPas(asin);
                    } catch (Exception e) {
                        //swallow
                    }
                }
                if (item != null)
                    updated = pricePas(updated, item, overrides);
            }
        }
        updated = productRepo.save(updated);
        return updated;
    }

    private Product initStub(String key, List<Attribute> value, Product parent) {

            Product child = new Product();
            child.setVariationType(VariationType.CHILD);
            CRC32 checksum = new CRC32();
            checksum.update(key.getBytes());
            long ref = checksum.getValue();
            child.slug(String.valueOf(ref)).ref(ref).merchantId(1L).active(true).sku(key).stub(true).inStock(true).title("stub");
            child.setVariationAttributes(value);
            child.setParent(parent);
            child.setMerchantId(1L);
            return child;
    }

    private MerchantStock getMerchantStock(Product product) {
        if(product.getMerchantStock() == null)
            return new MerchantStock();

        return product.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
    }

    private Product setMerchantStock(Product product, MerchantStock stock, BigDecimal quantity) {
        if(stock.getId() == null) {
            stock.setMerchantId(1L);
            product.addMerchantStock(stock.link("amazon.com/dp/"+product.getSku()));
        }
        long window = DEFAULT_WINDOW;
        if ( product.getExpires() != null)
            window = Math.abs(Duration.between(product.getUpdated(), product.getExpires()).getSeconds());

        if ( product.getPrice() != null ) {
            double diff = product.getPrice().subtract(stock.getPrice()).abs().doubleValue();
            BigDecimal bPercent = BigDecimal.ONE.subtract(product.getPrice().divide(stock.getPrice(),8, RoundingMode.HALF_EVEN).subtract(BigDecimal.ONE).abs());
            double percent = bPercent.doubleValue();

            if (diff < .6 || percent < .06)
                window *= 1 + (1 - percent);
            else
                window = BigDecimal.valueOf(window).divide(bPercent, 8, RoundingMode.HALF_EVEN).longValue();
        }

        product.setExpires(Instant.now().plusSeconds(window));
        product.setPrice(new Price(stock.getPrice(), "OMR"));

        return product;
    }

    private Product priceMws(Product p, List<ProductOverride> overrides) throws NoOfferException {

        if (p.getWeight() == null || p.getWeight().doubleValue() < PasUtility.MINWEIGHT) return p;
        MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;
        try {
            product = setMerchantStock(p, MwsLookupParser.parseStock(getMerchantStock(p),n, p.getWeight(), overrides), BigDecimal.valueOf(99L));
            product.inStock(true);
        } catch (PricingException e) {
            product.setPrice((BigDecimal) null);
            //e.printStackTrace();
        } catch (NoOfferException e) {
            //product = setMerchantStock(p, getMerchantStock(p),BigDecimal.ZERO);
            product.inStock(false);
        }
        return product;
    }

    private Product pricePas(Product p, PasItemNode item, List<ProductOverride> overrides) throws NoOfferException {

        if (p.getWeight() == null || p.getWeight().doubleValue() < PasUtility.MINWEIGHT) return p;
        //MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;

        MerchantStock stock = this.getMerchantStock(product);
        try {
            product = setMerchantStock(product, PasLookupParser.parseStock(product, stock, item, overrides),BigDecimal.valueOf(99L));
            product.inStock(true);
        } catch (PricingException e) {
            product.setPrice((BigDecimal) null);
            //e.printStackTrace();//@Todo set stock quantity to 0
        } catch (NoOfferException e) {
            //e.printStackTrace();
            product.inStock(false);
        }
        return product;
    }

    ProductLang getLang(Product product, final String lang) {
        if(product.getProductLangs() == null)
            return new ProductLang();
        return product.getProductLangs().stream().filter(x -> x.getLang().equalsIgnoreCase(lang)).findFirst().orElse(new ProductLang());
    }

    Product initProduct(Product product, PasItemNode item, boolean isParent, List<ProductOverride> overrides) {
        BigDecimal currentWeight = product.getWeight();
        product = (Product) PasLookupParser.parseProduct(product, item, isParent, overrides, 1L, "", "");

        if(product.getWeight() == null)
            product.setWeight(currentWeight);
        if((product.getWeight() == null || product.getWeight().doubleValue() < PasUtility.MINWEIGHT) && !isParent) {
            BigDecimal weight = productRepo.lookupWeight(product.getSku());
            product.setWeight(weight);
        }
        ProductLang en = getLang(product, "en");
        ProductLang ar = getLang(product, "ar");

        en = (ProductLang) PasLookupParser.parseProductI18n(en, item, "en");
        //ar = (ProductLang) PasLookupParser.parseProductI18n(ar, item, "ar");

        if(en.getId() == null) {
            product.addProductLang(en);
        }
/*        //if(ar.getId() == null) {
            ar = translate(en, "ar");
            product.addProductLang(ar);
        //}*/
        //if(item.get() == null || (!item.isSuperSaver() && !item.isPrime()))
          //  return product;
        if(isParent)
            return product;

        MerchantStock stock = this.getMerchantStock(product);
        try {
            product = setMerchantStock(product, PasLookupParser.parseStock(product, stock, item, overrides),BigDecimal.valueOf(99L));
        } catch (PricingException e) {
            //e.printStackTrace();//@Todo set stock quantity to 0
            System.out.println(e.getMessage());
        } catch (NoOfferException e) {
            //e.printStackTrace();
            System.out.println(e.getMessage());
        }
        product.setStub(false);
        return product;
    }

    private static Map<String, Item> parse_response(List<Item> items) {
        Map<String, Item> mappedResponse = new HashMap<String, Item>();
        for (Item item : items) {
            mappedResponse.put(item.getASIN(), item);
        }
        return mappedResponse;
    }


    public ProductResponse searchItems(String keyword) throws NoOfferException {
        SearchItemsResponse search = pasLookup.searchItems(keyword);
        if (search.getErrors() != null)
            throw new NoOfferException("No results found");

        ProductResponse response = new ProductResponse();
        List<ProductDTO> dtos = new ArrayList<ProductDTO>();

        for(Item x : search.getSearchResult().getItems()) {
            PasItemNode item = pasItemMapper.itemToPasItemNode(x);
            Product product = initProduct(new Product(), item, false, null);
            dtos.add(productMapper.toDto(product));
        }

        response.setItems(dtos);
        response.setTotal(dtos.size());
        response.setHasMore(false);

        return response;
    }
}
