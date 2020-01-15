package com.badals.shop.vendor.amazon;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.algolia.search.APIClient;
import com.algolia.search.ApacheAPIClientBuilder;
import com.algolia.search.Index;
import com.algolia.search.exceptions.AlgoliaException;
import com.algolia.search.objects.Query;
import com.badals.migration.entity.Product;
import com.badals.shop.entity.ProductBody;
import com.badals.shop.entity.ProductEntity;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.vendor.amazon.pas.mappings.ItemLookupResponse;
import com.badals.shop.vendor.amazon.pas.mappings.ItemNode;
import com.vladmihalcea.hibernate.type.json.internal.JacksonUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PasService {

    @Autowired
    ProductRepository productRepo;

    @Autowired
    private PasLookup pasLookup;

    public ProductEntity lookup(String asin) throws AlgoliaException {


        ItemLookupResponse doc = pasLookup.lookup(asin);
        List<ItemNode> is = doc.getItems().getItems();

        ProductEntity product = null;
        product = new ProductEntity();
        product.setIs_parent(0);

        for (ItemNode item : is) {
            ProductBody pBody = new ProductBody();
            pBody = PasLookupParser.parseProduct(item);

            if (item.getParentAsin() != null && item.getParentAsin() != asin)
                return lookup(item.getParentAsin());


            log.info("parent = " + item.getParentAsin());
            product.setIs_parent(1);
            if (item.getVariations() != null) {
                for (ItemNode childItem : item.getVariations().items) {
                    ProductBody cBody = PasLookupParser.parseProduct(childItem);
                    cBody.setParent(pBody.getId());

                    ProductEntity child = new ProductEntity();
                    child.setParent(pBody.getId());
                    child.setSku(cBody.getAsin());
                    child.setBody(cBody);

                    child.setRewrite(cBody.getId());
                    log.info("REWRITE VALUE " + child.getId());
                    child.setView_count(1);
                    child.setIs_parent(0);


                    productRepo.save(child);
                    pBody.addVariation(cBody.getId(), cBody.getVariationAttributes());
                }
            }

            product.setRewrite(pBody.getId());
            product.setView_count(1);
            log.info(pBody.getId().toString());
            log.info("parent = " + pBody.getParentAsin());

            product.setSku(pBody.getAsin());
            log.info(JacksonUtil.toString(pBody));
            product.setBody(pBody);
            productRepo.save(product);

        }
      /*PasIndex idx = new PasIndex();
      idx.setAsin(asin);
      idx.setDoc(is);
      idx.setObjectID(asin);

      index.addObject(idx);*/
        return product;
    }


    public Product lookupToPresta(String asin) throws AlgoliaException {
        return null;
    }
}
