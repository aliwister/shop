package com.badals.shop.service.mapper;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.*;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductLangMapper.class, MerchantStockMapper.class})
public interface AddProductMapper extends EntityMapper<AddProductDTO, Product> {

    @Mapping(target = "productLangs", ignore = true)
    @Mapping(target = "removeProductLang", ignore = true)
    @Mapping(target = "merchantStock", ignore = true)
    //@Mapping(target = "")
    Product toEntity(AddProductDTO productDTO);

    AddProductDTO toDto(Product product);

/*    @AfterMapping
    default void afterMapping(@MappingTarget Product target, AddProductDTO source) {
        if (target.getGallery() == null) {
            target.setGallery(new ArrayList<Gallery>());
        }
        target.getGallery().add(0, new Gallery(source.getImage()));

        // Process sale price and discount percentage
        MerchantStock stock = new MerchantStock();
        int discount = 0;


        stock.quantity(source.getQuantity()).availability(source.getAvailability()).cost(source.getCost()).allow_backorder(false)
                .price(source.getSalePrice()).discount(source.getPrice().subtract(source.getSalePrice()).divide(source.getPrice()).multiply(BigDecimal.valueOf(100L)).intValue());

        stock.setMerchantId(5L);//TenantContext.getCurrentMerchantId());
        target.addMerchantStock(stock);



        ProductLang langAr = new ProductLang().lang("ar").description(source.getDescription_ar()).title(source.getName_ar());
        if(source.getFeatures_ar() != null)
            langAr.setFeatures(Arrays.asList(source.getFeatures_ar().split(";")));

        ProductLang langEn = new ProductLang().lang("en").description(source.getDescription()).title(source.getName());
        if(source.getFeatures() != null)
            langEn.setFeatures(Arrays.asList(source.getFeatures().split(";")));

        target.addProductLang(langAr);
        target.addProductLang(langEn);
    }*/

    @AfterMapping
    default void afterMapping(@MappingTarget AddProductDTO target, Product source) {
        if (target.getGallery() == null) {
            target.setGallery(new ArrayList<Gallery>());
        }
        target.getGallery().add(0, new Gallery(source.getImage()));

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
