package com.badals.shop.service.mapper;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;

import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.TenantProduct;
import com.badals.shop.domain.TenantStock;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.ChildProduct;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductLangMapper.class, MerchantStockMapper.class, ProductLangMapper.class})
public interface ChildProductMapper extends EntityMapper<ChildProduct, TenantProduct> {

    //@Mapping(target = "productLangs", ignore = true)
    //@Mapping(target = "removeProductLang", ignore = true)
    //@Mapping(target = "merchantStock", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "title", source = "name")
    @Mapping(target = "price", source = "priceObj")
    //@Mapping(target = "currency", source = "priceObj.currency")
    TenantProduct toEntity(ChildProduct productDTO);

    @Mapping(source = "price", target = "priceObj")
    //@Mapping(source = "currency", target = "priceObj.currency")
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "price", ignore = true)
    ChildProduct toDto(TenantProduct product);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantProduct target, ChildProduct source) {
        if(source.getGallery() != null) {
            List<Gallery> gallery = new ArrayList<Gallery>();
            for(String g: source.getGallery()) {
                gallery.add(new Gallery(g));
            }
            target.setGallery(gallery);
        }

        Price price = source.priceObj;
        assert(price != null);

        if(price != null) {
            Double dPrice = price.getAmount().doubleValue();
            Price cost = source.costObj;
            Double salePrice = price.getAmount().doubleValue();
            if (source.getSalePrice() != null ) {
                salePrice = source.getSalePrice().doubleValue();
            }
            int discount = 100 * (int)((dPrice-salePrice)/dPrice);
            target.getStock().add(new TenantStock().quantity(source.getQuantity()).availability(source.getAvailability()).cost(cost).allow_backorder(false)
                    .price(price).product(target));
        }


    }

    @AfterMapping
    default void afterMapping(@MappingTarget ChildProduct target, TenantProduct source) {
        //if (source.getGallery() == null) {
        List<String> gallery = new ArrayList<String>();
        //}
        //target.getGallery().add(0, source.getImage());
        if(source.getGallery() != null) {
            for (Gallery g : source.getGallery()) {
                gallery.add(g.getUrl());
            }
            target.setGallery(gallery);
        }

        // Process sale price and discount percentage
        TenantStock stock = source.getStock().stream().findFirst().orElse(null);
        if (stock != null) {
            target.setSalePriceObj(stock.getPrice());
            target.setPriceObj(source.getPrice());
            target.setDiscountInPercent(0);
            target.setCostObj(stock.getCost());
            target.setQuantity(stock.getQuantity());

/*            if(stock.getDiscount() != null) {
                int discount = stock.getDiscount();
                target.setDiscountInPercent(discount);
                //target.setPrice(new BigDecimal((int)(10*stock.getPrice().doubleValue()/(1.0-discount*.01))/10.0 ));
            }*/
            int hours = stock.getAvailability();
            /*
            @Todo
            Move to language files
             */
            target.setAvailability(hours);
        }
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
