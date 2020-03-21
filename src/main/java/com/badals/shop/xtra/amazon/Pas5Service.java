package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.amazon.paapi5.v1.Item;
import com.amazon.paapi5.v1.VariationsResult;
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
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.xtra.IProductService;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsLookupParser;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookupParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class Pas5Service implements IProductService {

    private final Logger log = LoggerFactory.getLogger(Pas5Service.class);

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    ProductOverrideRepository productOverrideRepository;

    @Autowired
    private PasLookup pasLookup;

    @Autowired
    private MwsLookup mwsLookup;

    @Autowired
    private RedisPasRepository redisPasRepository;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private PasItemMapper pasItemMapper;

    public Product lookup(String asin, boolean isRedis, boolean isRebuild) {
        return lookup(asin, false, isRedis, isRebuild);
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

    @Transactional
    public Product lookup(String asin, boolean isParent, boolean isRedis, boolean isRebuild) {

        Product product = productRepo.findBySkuJoinChildren(asin).orElse(null);
        if (product == null) {
            if(isParent) isRebuild = true;
            product = new Product();
        }

        //if (product != null) // && product.getUpdated())
        List<Product> mws = new ArrayList<>();
        GetItemsResponse response = null;
        PasItemNode item = null;
        Map<String, Item> doc;

        // Read from redis cache
        if (isRedis && redisPasRepository.getHashOperations().hasKey("pas", asin)) {
            try {
                item = (PasItemNode) redisPasRepository.getHashOperations().get("pas", asin);
            }
            catch (Exception e) {
                redisPasRepository.getHashOperations().delete("pas", asin);
            }
        }

        List<String> list = new ArrayList();
        list.add(asin);

        if (item == null) {
            response = pasLookup.lookup(list);
            doc = parse_response(response.getItemsResult().getItems());
            item = pasItemMapper.itemToPasItemNode(doc.get(asin));
            redisPasRepository.getHashOperations().put("pas", asin, item);
        }

        //for (String itemId : list) {
            //Map<String, Item> responseList = parse_response(response.getItemsResult().getItems());

        if(isParent)
            product.setVariationType(VariationType.PARENT);
        else
            product.setVariationType(VariationType.SIMPLE);

        if (item.getParentAsin() != null && !item.getParentAsin().equals("asin")) {
            if(!existsBySku(item.getParentAsin()) || isRebuild)
               return lookup(item.getParentAsin(), true, isRedis, isRebuild);
            product.setVariationType(VariationType.CHILD);
            product.setParentId(findBySku(item.getParentAsin()).getRef());
        }

        List<ProductOverride> overrides = findOverrides(item.getAsin(), item.getParentAsin());
        product = initProduct(product, item, isParent, overrides);

        if(isRebuild) {
            GetVariationsResponse variationsResponse = pasLookup.variationLookup(asin, 1);
            if (isParent) {
                //get dimensions
                PasLookupParser.parseDimensions(product, variationsResponse);
            }
            if (variationsResponse != null && variationsResponse.getVariationsResult() != null) {
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
                        String childAsin = childItem.getAsin();
                        Product child = children.stream().filter(x -> x.getSku().equals(childAsin)).findFirst().orElse(new Product());
                        child = initProduct(child, childItem, false, overrides);

                        PasLookupParser.parseVariationAttributes(child, childItem);
                        variations.add(new Variation(child.getRef(), child.getVariationAttributes()));
                        child.setParent(product);
                        child.setParentId(product.getRef());

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
                product.setVariationType(VariationType.PARENT);
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

    MerchantStock getMerchantStock(Product product) {
        if(product.getMerchantStock() == null)
            return new MerchantStock();

        return product.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
    }

    Product setMerchantStock(Product product, MerchantStock stock) {
        if(stock.getId() == null) {
            stock.setMerchantId(1L);
            product.addMerchantStock(stock.link("amazon.com/dp/"+product.getSku()));
        }
        product.setPrice(new Price(stock.getPrice(), "OMR"));

        return product;
    }

    Product priceMws(Product p, List<ProductOverride> overrides) {
        if (p.getWeight() == null) return p;
        MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;
        try {
            product = setMerchantStock(p, MwsLookupParser.parseStock(getMerchantStock(p),n, p.getWeight(), overrides));
        } catch (PricingException e) {
            //e.printStackTrace();
        } catch (NoOfferException e) {
            //e.printStackTrace();
        }
        return product;
    }

    ProductLang getLang(Product product) {
        if(product.getProductLangs() == null)
            return new ProductLang();
        return product.getProductLangs().stream().findFirst().orElse(new ProductLang());
    }

    Product initProduct(Product product, PasItemNode item, boolean isParent, List<ProductOverride> overrides) {
        product = (Product) PasLookupParser.parseProduct(product, item, isParent);
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
            product = setMerchantStock(product, PasLookupParser.parseStock(stock, item, overrides));
        } catch (PricingException e) {
            //e.printStackTrace();
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
