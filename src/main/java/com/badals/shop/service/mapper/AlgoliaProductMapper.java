package com.badals.shop.service.mapper;

import com.amazon.paapi5.v1.Item;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.AlgoliaProduct;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.service.dto.ProductDTO;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlgoliaProductMapper {


    @Mapping(target = "price", ignore = true)
    @Mapping(target = "hierarchicalCategories", ignore = true)
    @Mapping(target = "availability", ignore = true)
    AlgoliaProduct producttoAlgoliaProduct(Product product);

    @AfterMapping
    default void afterMapping(@MappingTarget AlgoliaProduct target, Product source) {
        ProductLang lang = source.getProductLangs().stream().findFirst().orElse(null);
        if(lang != null) {
            target.setTitle(lang.getTitle());
            String t = "";
            Map cats = new HashMap<String, String>();
            String[] s = lang.getBrowseNode().split(">");
            int i = 1;
            for (String l : s) {
                if (t.isEmpty()) {
                    t += l;
                } else {
                    t += " > " + l;
                }

                cats.put("lvl" + i++, t.trim());
            }
            target.setHierarchicalCategories(cats);
        }

        if(source.getPrice() != null) {
            Map price = new HashMap<String, String>();
            price.put("OMR", source.getPrice());
            target.setPrice(price);
        }

        MerchantStock stock = source.getMerchantStock().stream().findFirst().orElse(null);
        if(stock.getAvailability() != null) {
            Map<String, String> map = ProductMapper.processAvailability(stock.getAvailability(), null);
            target.setAvailability(map.get("en"));
            target.setAvailability_ar(map.get("ar"));
        }
    }
}
