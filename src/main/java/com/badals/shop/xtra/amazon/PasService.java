package com.badals.shop.xtra.amazon;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.repository.ProductRepository;

import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.xtra.IProductService;
import com.badals.shop.xtra.amazon.mappings.ItemLookupResponse;
import com.badals.shop.xtra.amazon.mappings.ItemNode;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class PasService implements IProductService {

    private final Logger log = LoggerFactory.getLogger(PasService.class);

    @Autowired
    ProductRepository productRepo;

    @Autowired
    private PasLookup pasLookup;

    @Autowired
    private RedisPasRepository redisPasRepository;

    private final ProductMapper productMapper;

    public PasService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public ProductDTO lookup(String asin) {

        Product product = productRepo.findBySku(asin).orElse(new Product());
        //if (product != null) // && product.getUpdated())

        // Read from redis cache
        ItemLookupResponse doc = null;
        if (redisPasRepository.getHashOperations().hasKey("pas", asin)) {
            doc = pasLookup.toXml(redisPasRepository.getHashOperations().get("pas", asin));
        }
        if (doc == null) {
            doc = pasLookup.lookup(asin);
            doc.setSku(asin);
            redisPasRepository.getHashOperations().put("pas", asin, pasLookup.toXML(doc));
        }

        List<ItemNode> is = doc.getItems().getItems();

        for (ItemNode item : is) {
            product = initProduct(product, item);

            if (item.getParentAsin() != null && item.getParentAsin() != asin)
                return lookup(item.getParentAsin());

            if (item.getVariations() != null) {
                List<Product> children = new ArrayList<Product>();
                List<Variation> variations = new ArrayList<Variation>();
                for (ItemNode childItem : item.getVariations().items) {
                    Product child = new Product();
                    child = initProduct(child, childItem);
                    variations.add(new Variation(child.getRef(), child.getVariationAttributes()));
                    child.setParent(product);
                    child.setParentId(product.getRef());
                    children.add(child);
                }
                product.setChildren(children);
            }
            productRepo.save(product);
        }
        return productMapper.toDto(product);
    }

    Product initProduct(Product product, ItemNode item) {
        product = (Product) PasLookupParser.parseProduct(product, item);
        ProductLang lang = new ProductLang();
        lang = (ProductLang) PasLookupParser.parseProductI18n(lang, item);
        lang.setProduct(product);
        product.setProductLangs(new HashSet<>(Arrays.asList(lang)));
        return product;
    }
}
