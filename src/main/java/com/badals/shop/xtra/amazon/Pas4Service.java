package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.Item;
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

import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsLookupParser;
import com.badals.shop.xtra.amazon.paapi4.Pas4Lookup;
import com.badals.shop.xtra.amazon.paapi4.Pas4LookupParser;


import com.badals.shop.xtra.amazon.paapi4.PasItemNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class Pas4Service implements IProductService {

    private final Logger log = LoggerFactory.getLogger(Pas4Service.class);

    @Autowired
    ProductRepository productRepo;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MerchantRepository merchantRepository;

    @Autowired
    private Pas4Lookup pasLookup;

    @Autowired
    private MwsLookup mwsLookup;

    @Autowired
    private RedisPasRepository redisPasRepository;

    @Autowired
    private ProductMapper productMapper;

    public ProductDTO lookup(String asin) {
        return lookup(asin, false);
    }

    @Transactional
    public ProductDTO lookup(String asin, boolean isParent) {

        Product product = productRepo.findBySkuJoinChildren(asin).orElse(new Product());
        //if (product != null) // && product.getUpdated())
        List<Product> mws = new ArrayList<>();
        PasItemNode item = pasLookup.lookup(asin);
        if(isParent)
            product.setVariationType(VariationType.PARENT);
        else
            product.setVariationType(VariationType.SIMPLE);
        //List<PasItemNode> is = doc.getItems().getItems();

       // for (ItemNode item : is) {
            product = initProduct(product, item, isParent);

            if (item.getParentAsin() != null && !item.getParentAsin().equals(asin))
                return lookup(item.getParentAsin(), true);

            if (item.getChildren() != null && item.getChildren().size() > 0) {
                Set<Product> children = product.getChildren();
                if (children == null)
                    children = new HashSet<Product>();
                List<Variation> variations = new ArrayList<Variation>();
                for (PasItemNode childItem : item.getChildren()) {
                    String childAsin = childItem.getAsin();
                    Product child = children.stream().filter(x -> x.getSku().equals(childAsin)).findFirst().orElse(new Product());
                    child = initProduct(child, childItem, false);
                    Pas4LookupParser.parseVariationAttributes(child, childItem);
                    variations.add(new Variation(child.getRef(), child.getVariationAttributes()));
                    child.setParent(product);
                    child.setParentId(product.getRef());
                    //child.addCategory(category);
                    if(child.getId() == null)
                        children.add(child);
                    child.setVariationType(VariationType.CHILD);
                    if(child.getPrice() == null)
                        mws.add(child);
                }
                   
                product.setChildren(children);
                product.setVariations(variations);
                product.setVariationType(VariationType.PARENT);
            }
            if(!isParent && product.getPrice() == null)
                mws.add(product);

            for(Product p: mws) {
                p = priceMws(p);
            }

            //product.setCategories(new HashSet<>().add(category));
            product = this.save(product);
        //}
        return productMapper.toDtoWOCategories(product);
    }

    @Transactional
    Product save(Product product) {
        return this.productRepo.save(product);
    }

    MerchantStock getMerchantStock(Product product) {
        if(product.getMerchantStock() == null)
            return new MerchantStock();

        return product.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
    }

    Product setMerchantStock(Product product, MerchantStock stock) {
        if(stock.getId() == null) {
            stock.setMerchantId(1L);
            product.addMerchantStock(stock);
        }
        product.setPrice(new Price(stock.getPrice(), "OMR"));
        return product;
    }

    ProductLang getLang(Product product) {
        if(product.getProductLangs() == null)
            return new ProductLang();
        return product.getProductLangs().stream().findFirst().orElse(new ProductLang());
    }

    Product priceMws(Product p) {
        if (p.getWeight() == null) return p;
        MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;
        /*try {
            //product = setMerchantStock(p, MwsLookupParser.parseStock(getMerchantStock(p),n, p.getWeight()));
        } catch (PricingException e) {
            e.printStackTrace();
        } catch (NoOfferException e) {
            e.printStackTrace();
        }*/
        return product;
    }

    Product initProduct(Product product, PasItemNode item, boolean isParent) {
        product = (Product) Pas4LookupParser.parseProduct(product, item, isParent);
        ProductLang lang = getLang(product);

        lang = (ProductLang) Pas4LookupParser.parseProductI18n(lang, item);

        if(lang.getId() == null) {
            product.addProductLang(lang);
        }

        if(item.getCost() == null || (!item.isSuperSaver() && !item.isPrime()))
           return product;
        if(isParent)
            return product;

        MerchantStock stock = this.getMerchantStock(product);
        try {
            product = setMerchantStock(product, Pas4LookupParser.parseStock(stock, item));
        } catch (PricingException e) {
            e.printStackTrace();
        } catch (NoOfferException e) {
            e.printStackTrace();
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
