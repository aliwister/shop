package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.repository.CategoryRepository;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.xtra.IProductService;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsLookupParser;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookupParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Pas5Service implements IProductService {

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


    public Pas5Service(ProductRepository productRepo, CategoryRepository categoryRepository, MerchantRepository merchantRepository, ProductOverrideRepository productOverrideRepository, PasLookup pasLookup, MwsLookup mwsLookup, RedisPasRepository redisPasRepository, ProductMapper productMapper, PasItemMapper pasItemMapper) {
        this.productRepo = productRepo;
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.productOverrideRepository = productOverrideRepository;
        this.pasLookup = pasLookup;
        this.mwsLookup = mwsLookup;
        this.redisPasRepository = redisPasRepository;
        this.productMapper = productMapper;
        this.pasItemMapper = pasItemMapper;
    }

    public Boolean existsBySku(String sku) {
        return productRepo.existsBySku(sku);
    }

    public Product findBySku(String sku) {
        return productRepo.findOneBySku(sku).get();
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

    @Transactional
    public Product lookup(String asin, boolean isParent, boolean isRedis, boolean isRebuild) throws NoOfferException {

        Product product = productRepo.findBySkuJoinChildren(asin).orElse(null);

        if(true)
            return mwsItemShortCircuit(product, asin, isParent, isRebuild);

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
                    mwsLookup.lookup(asin);
                    return null;
                }
            }
            doc = parse_response(response.getItemsResult().getItems());
            item = pasItemMapper.itemToPasItemNode(doc.get(asin));
            redisPasRepository.getHashOperations().put("pas", asin, item);
        }

        //for (String itemId : list) {
            //Map<String, Item> responseList = parse_response(response.getItemsResult().getItems());

        if(isParent)
            isParent = setVariationType(product, VariationType.PARENT);
        else
            isParent = setVariationType(product, VariationType.SIMPLE);

        if (item.getParentAsin() != null && !item.getParentAsin().equals("asin")) {
            if(!existsBySku(item.getParentAsin()) || isRebuild)
               return lookup(item.getParentAsin(), true, isRedis, isRebuild);
            product.setVariationType(VariationType.CHILD);
            product.setParentId(findBySku(item.getParentAsin()).getRef());
        }

        List<ProductOverride> overrides = findOverrides(item.getId(), item.getParentAsin());
        product = initProduct(product, item, isParent, overrides);

        if(isRebuild) {
            GetVariationsResponse variationsResponse = pasLookup.variationLookup(asin, 1);
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
                        Product child = children.stream().filter(x -> x.getSku().equals(childAsin)).findFirst().orElse(new Product());
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

    private Product mwsItemShortCircuit(Product product, String asin, boolean isParent, boolean isRebuild) throws NoOfferException {
        if(product == null) {
            PasItemNode item = mwsLookup.lookup(asin);
            product = new Product();
            initProduct(product, item, false, null);
            product.setVariationType(VariationType.SIMPLE);
        }
        List<ProductOverride> overrides = findOverrides(asin, null);
        product = priceMws(product, overrides);
        product = productRepo.save(product);
        return product;
    }

    MerchantStock getMerchantStock(Product product) {
        if(product.getMerchantStock() == null)
            return new MerchantStock();

        return product.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
    }

    Product setMerchantStock(Product product, MerchantStock stock, BigDecimal quantity) {
        if(stock.getId() == null) {
            stock.setMerchantId(1L);
            product.addMerchantStock(stock.link("amazon.com/dp/"+product.getSku()));
        }
        product.setPrice(new Price(stock.getPrice(), "OMR"));

        return product;
    }

    Product priceMws(Product p, List<ProductOverride> overrides) throws NoOfferException {

        if (p.getWeight() == null || p.getWeight().doubleValue() < .01) return p;
        MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;
        try {
            product = setMerchantStock(p, MwsLookupParser.parseStock(getMerchantStock(p),n, p.getWeight(), overrides), BigDecimal.valueOf(99L));
        } catch (PricingException e) {
            product.setPrice((BigDecimal) null);
            //e.printStackTrace();
        } catch (NoOfferException e) {
            //product = setMerchantStock(p, getMerchantStock(p),BigDecimal.ZERO);
            if(p.getVariationType() == VariationType.SIMPLE)
                throw e;
        }
        return product;
    }

    ProductLang getLang(Product product) {
        if(product.getProductLangs() == null)
            return new ProductLang();
        return product.getProductLangs().stream().findFirst().orElse(new ProductLang());
    }

    Product initProduct(Product product, PasItemNode item, boolean isParent, List<ProductOverride> overrides) {
        product = (Product) PasLookupParser.parseProduct(product, item, isParent, overrides);
        if((product.getWeight() == null || product.getWeight().doubleValue() < .001) && !isParent) {
            BigDecimal weight = productRepo.lookupWeight(product.getSku());
            product.setWeight(weight);
        }
        ProductLang lang = getLang(product);

        lang = (ProductLang) PasLookupParser.parseProductI18n(lang, item);

        if(lang.getId() == null) {
            product.addProductLang(lang);
        }
        //if(item.get() == null || (!item.isSuperSaver() && !item.isPrime()))
          //  return product;
        if(isParent)
            return product;

        MerchantStock stock = this.getMerchantStock(product);
        try {
            product = setMerchantStock(product, PasLookupParser.parseStock(product, stock, item, overrides),BigDecimal.valueOf(99L));
        } catch (PricingException e) {
            //e.printStackTrace();//@Todo set stock quantity to 0
        } catch (NoOfferException e) {
            //e.printStackTrace();
        }

        return product;
    }

    private static Map<String, Item> parse_response(List<Item> items) {
        Map<String, Item> mappedResponse = new HashMap<String, Item>();
        for (Item item : items) {
            mappedResponse.put(item.getASIN(), item);
        }
        return mappedResponse;
    }
}
