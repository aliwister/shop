package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.amazon.paapi5.v1.Item;
import com.amazon.paapi5.v1.VariationsResult;
import com.badals.shop.domain.Category;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.repository.CategoryRepository;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.repository.ProductRepository;

import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.xtra.IProductService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PasService implements IProductService {

    private final Logger log = LoggerFactory.getLogger(PasService.class);

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    private PasLookup pasLookup;

    @Autowired
    private RedisPasRepository redisPasRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductDTO lookup(String asin) {
        return lookup(asin, false);
    }

    @Transactional
    public ProductDTO lookup(String asin, boolean isParent) {

        Product product = productRepo.findBySkuJoinCategories(asin).orElse(new Product());
        //if (product != null) // && product.getUpdated())

        // Read from redis cache
        GetItemsResponse doc = null;
        //if (redisPasRepository.getHashOperations().hasKey("pas", asin)) {
            //doc = pasLookup.toXml(redisPasRepository.getHashOperations().get("pas", asin));
        //}
        List<String> list = new ArrayList();
        list.add(asin);
        if (doc == null) {
            doc = pasLookup.lookup(list);
            //doc.setSku(asin);
            //redisPasRepository.getHashOperations().put("pas", asin, pasLookup.toXML(doc));
        }

        GetItemsResponse response = doc;

        for (String itemId : list) {
            Map<String, Item> responseList = parse_response(response.getItemsResult().getItems());
            Item item = responseList.get(itemId);


            if(isParent)
                product.setVariationType(VariationType.PARENT);
            else
                product.setVariationType(VariationType.SIMPLE);

            if (item.getParentASIN() != null && item.getParentASIN() != itemId)
                return lookup(item.getParentASIN(), true);

            product = initProduct(product, item, isParent);
            GetVariationsResponse variationsResponse = pasLookup.variationLookup(itemId, 1);
            if (isParent) {
                //get dimensions
                PasLookupParser.parseDimensions(product, variationsResponse);
            }
            //Category category = categoryRepository.getOne(1L);

            if (variationsResponse != null) {
                int page = 1;
                boolean more = true;
                List<Product> children = new ArrayList<Product>();
                List<Variation> variations = new ArrayList<Variation>();
                while (more) {
                    VariationsResult variationsResult = variationsResponse.getVariationsResult();
                    if(variationsResult == null)
                        break;
                    for (Item childItem : variationsResult.getItems()) {
                        Product child = new Product();
                        child = initProduct(child, childItem, false);
                        PasLookupParser.parseVariationAttributes(child, childItem);
                        variations.add(new Variation(child.getRef(), child.getVariationAttributes()));
                        child.setParent(product);
                        child.setParentId(product.getRef());
                        //child.addCategory(category);
                        children.add(child);
                        child.setVariationType(VariationType.CHILD);
                    }
                    log.info("This variation set had {} items", variationsResult.getItems().size());
                    if(variationsResult.getItems().size() < 10)
                        more = false;
                    else {
                        page++;
                        variationsResponse = pasLookup.variationLookup(itemId, page);
                    }
                }
                product.setChildren(children);
                product.setVariations(variations);
            }
            product.setVariationType(VariationType.PARENT);
            //product.setCategories(new HashSet<>().add(category));
            product = productRepo.save(product);
        }
        return productMapper.toDtoWOCategories(product);
    }

    Product initProduct(Product product, Item item, boolean isParent) {
        product = (Product) PasLookupParser.parseProduct(product, item, isParent);
        ProductLang lang = new ProductLang();
        lang = (ProductLang) PasLookupParser.parseProductI18n(lang, item);
        lang.setProduct(product);
        product.setProductLangs(new HashSet<>(Arrays.asList(lang)));

        MerchantStock stock = new MerchantStock();
        if(!isParent)
            try {
                stock = PasLookupParser.parseStock(stock, item);

                stock.setMerchant(merchantRepository.getOne(1L));
                product.addMerchantStock(stock);
                product.setPrice(new Price(stock.getPrice(), "OMR"));
            }
            catch(PricingException e) {
                //swallow
            }
            catch(NoOfferException e) {
                //swallow
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
