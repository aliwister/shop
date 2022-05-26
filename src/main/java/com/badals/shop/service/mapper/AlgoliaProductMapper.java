package com.badals.shop.service.mapper;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.AlgoliaProduct;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.service.dto.ProductDTO;

import com.badals.shop.service.pojo.AddProductDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.RoundingMode;
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
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(target = "attributes", ignore = true)
    AlgoliaProduct producttoAlgoliaProduct(Product product);

    @AfterMapping
    default void afterMapping(@MappingTarget AlgoliaProduct target, Product source) {

        source.getProductLangs().stream().forEach(
            x -> {
                target.getTitle().put(x.getLang(), x.getTitle());
                target.getBrand().put(x.getLang(), x.getBrand());
                target.getHierarchicalCategories().put(x.getLang(), parseCategories(x.getBrowseNode()));
            }
        );

        MerchantStock stock = source.getMerchantStock().stream().findFirst().orElse(null);
        target.setAvailability(stock.getAvailability());

        if(source.getPrice() != null) {
            Map price = new HashMap<String, String>();
            stock.getPrices().forEach(
                    x-> price.put(x.getCurrency(), x.getAmount())
            );
            target.setPrice(price);
        }

    }

    private Map<String, String> parseCategories(String browseNode) {
        String t = "";
        Map cats = new HashMap<String, String>();
        String[] s = browseNode.split(">");
        int i = 1;
        for (String l : s) {
            if (t.isEmpty()) {
                t += l;
            } else {
                t += " > " + l;
            }

            cats.put("lvl" + i++, t.trim());
        }
        return cats;
    }

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "brand", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(source="availability", target = "availability")
    AlgoliaProduct addProductToAlgoliaProduct(AddProductDTO doc);

    @AfterMapping
    default void afterMapping(@MappingTarget AlgoliaProduct target, AddProductDTO source) {
        //ProductLang lang = source.getProductLangs().stream().findFirst().orElse(null);
        if(source.getBrowseNode() != null) {
            String t = "";
            Map cats = new HashMap<String, String>();
            String[] s = source.getBrowseNode().split(">");
            int i = 1;
            for (String l : s) {
                if (t.isEmpty()) {
                    t += l;
                } else {
                    t += " > " + l;
                }

                cats.put("lvl" + i++, t.trim());
            }
            target.getHierarchicalCategories().put("en", cats);
        }
        if(source.getBrowseNode_ar() != null) {
            String t = "";
            Map cats = new HashMap<String, String>();
            String[] s = source.getBrowseNode_ar().split(">");
            int i = 1;
            for (String l : s) {
                if (t.isEmpty()) {
                    t += l;
                } else {
                    t += " > " + l;
                }

                cats.put("lvl" + i++, t.trim());
            }
            target.getHierarchicalCategories().put("ar", cats);
        }

        if(source.getSalePrice()!= null) {
            Map price = new HashMap<String, String>();
            price.put("OMR", source.getSalePrice().setScale(2, RoundingMode.HALF_UP));
            target.setPrice(price);
        }


/*        if(source.getAvailability() != null) {
            Map<String, String> map = ProductMapper.processAvailability(source.getAvailability());
            target.setAvailability(map.get("en"));
            target.setAvailability_ar(map.get("ar"));
        }*/
    }
}
