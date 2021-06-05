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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

@Service
public class PasUKService implements IProductService {

    private final Logger log = LoggerFactory.getLogger(PasUKService.class);
    private final ProductRepository productRepo;
    private final MerchantRepository merchantRepository;
    private final ProductOverrideRepository productOverrideRepository;


    private final PasLookup pasLookup;

    private final ProductMapper productMapper;
    private final PasItemMapper pasItemMapper;


    public PasUKService(ProductRepository productRepo, MerchantRepository merchantRepository, ProductOverrideRepository productOverrideRepository, @Qualifier("uk") PasLookup pasLookup, ProductMapper productMapper, PasItemMapper pasItemMapper) {
        this.productRepo = productRepo;
        this.merchantRepository = merchantRepository;
        this.productOverrideRepository = productOverrideRepository;
        this.pasLookup = pasLookup;

        this.productMapper = productMapper;
        this.pasItemMapper = pasItemMapper;
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

    @Transactional
    public Product lookup(String asin, boolean isParent, boolean isRedis, boolean isRebuild, boolean forcePas) throws NoOfferException, PricingException {

        Product product = productRepo.findBySkuJoinChildren(asin, 1L).orElse(null);

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

        List<Product> mws = new ArrayList<>();
        GetItemsResponse response = null;
        PasItemNode item = null;
        Map<String, Item> doc;

        List<String> list = new ArrayList();
        list.add(asin);

        if (item == null) {
            response = pasLookup.lookup(list);
            if(response.getErrors() != null && response.getErrors().size() > 0 ) {
                ErrorData error = response.getErrors().get(0);
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

    Product pricePas(Product p, PasItemNode item, List<ProductOverride> overrides) throws NoOfferException {

        if (p.getWeight() == null || p.getWeight().doubleValue() < .01) return p;
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

    ProductLang getLang(Product product) {
        if(product.getProductLangs() == null)
            return new ProductLang();
        return product.getProductLangs().stream().findFirst().orElse(new ProductLang());
    }

    Product initProduct(Product product, PasItemNode item, boolean isParent, List<ProductOverride> overrides) {
        BigDecimal currentWeight = product.getWeight();
        product = (Product) PasLookupParser.parseProduct(product, item, isParent, overrides, 1L, "", "");

        if(product.getWeight() == null)
            product.setWeight(currentWeight);
        if((product.getWeight() == null || product.getWeight().doubleValue() < .001) && !isParent) {
            BigDecimal weight = productRepo.lookupWeight(product.getSku());
            product.setWeight(weight);
        }
        ProductLang lang = getLang(product);

        lang = (ProductLang) PasLookupParser.parseProductI18n(lang, item, "en");

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
}
