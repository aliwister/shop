package com.badals.shop.service.mapper;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.*;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductLangMapper.class, MerchantStockMapper.class})
public interface AddProductMapper extends EntityMapper<AddProductDTO, Product> {

    @Mapping(target = "productLangs", ignore = true)
    @Mapping(target = "removeProductLang", ignore = true)
    @Mapping(target = "merchantStock", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    //@Mapping(target = "")
    Product toEntity(AddProductDTO productDTO);

    @Mapping(target = "gallery", ignore = true)
    AddProductDTO toDto(Product product);

    @AfterMapping
    default void afterMapping(@MappingTarget Product target, AddProductDTO source) {
        if(source.getGallery() != null) {
            List<Gallery> gallery = new ArrayList<Gallery>();
            for(String g: source.getGallery()) {
                gallery.add(new Gallery(g));
            }
            target.setGallery(gallery);
        }
    }

    @AfterMapping
    default void afterMapping(@MappingTarget AddProductDTO target, Product source) {
        //if (source.getGallery() == null) {
        List<String> gallery = new ArrayList<String>();
        //}
        //target.getGallery().add(0, source.getImage());
        for(Gallery g: source.getGallery()) {
            gallery.add(g.getUrl());
        }
        target.setGallery(gallery);

        // Process sale price and discount percentage
        MerchantStock stock = source.getMerchantStock().stream().findFirst().orElse(null);
        if (stock != null) {
            target.setSalePrice(stock.getPrice());
            target.setPrice(stock.getPrice());
            target.setDiscountInPercent(0);
            target.setCost(stock.getCost());
            target.setQuantity(stock.getQuantity());

            if(stock.getDiscount() != null) {
                int discount = stock.getDiscount();
                target.setDiscountInPercent(discount);
                target.setPrice(new BigDecimal((int)(10*stock.getPrice().doubleValue()/(1.0-discount*.01))/10.0 ));
            }
            int hours = stock.getAvailability();
            /*
            @Todo
            Move to language files
             */
            target.setAvailability(hours);
        }

        //ProductLang lang = source.getProductLangs().stream().findFirst().orElse(null);
        for(ProductLang lang : source.getProductLangs()) {
            if(lang.getLang().equals("en")) {
                if(lang.getFeatures() != null)
                    target.setFeatures(String.join(";",lang.getFeatures()));
                target.setName(lang.getTitle());
                target.setDescription(lang.getDescription());
                target.setBrand(lang.getBrand());
            }
            else if(lang.getLang().equals("ar")) {
                if(lang.getFeatures() != null)
                    target.setFeatures_ar(String.join(";",lang.getFeatures()));
                target.setName_ar(lang.getTitle());
                target.setDescription_ar(lang.getDescription());
                target.setBrand_ar(lang.getBrand());
            }
        }
        if(source.getCategories() != null)
            target.setShopIds(source.getCategories().stream().map(Category::getId).collect(Collectors.toList()));

    }

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product purchase = new Product();
        purchase.setId(id);
        return purchase;
    }
}
