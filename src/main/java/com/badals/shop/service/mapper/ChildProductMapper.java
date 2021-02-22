package com.badals.shop.service.mapper;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Price;
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
public interface ChildProductMapper extends EntityMapper<ChildProduct, Product> {

    @Mapping(target = "productLangs", ignore = true)
    @Mapping(target = "removeProductLang", ignore = true)
    @Mapping(target = "merchantStock", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "title", source = "name")
    @Mapping(target = "price", source = "priceObj.amount")
    @Mapping(target = "currency", source = "priceObj.currency")
    @Mapping(target = "dial", ignore = true)
    Product toEntity(ChildProduct productDTO);

    @Mapping(source = "price", target = "priceObj.amount")
    @Mapping(source = "currency", target = "priceObj.currency")
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(source="dial.dial", target="dial")
    ChildProduct toDto(Product product);

    @AfterMapping
    default void afterMapping(@MappingTarget Product target, ChildProduct source) {
        if(source.getGallery() != null) {
            List<Gallery> gallery = new ArrayList<Gallery>();
            for(String g: source.getGallery()) {
                gallery.add(new Gallery(g));
            }
            target.setGallery(gallery);
        }

        BigDecimal price = source.priceObj.getAmount();
        assert(price != null);

        Double dPrice = price.doubleValue();
        BigDecimal cost = source.costObj.getAmount();
        Double salePrice = price.doubleValue();
        if(source.getSalePriceObj() != null && source.getSalePriceObj().getAmount() != null) {
            salePrice = source.getSalePriceObj().getAmount().doubleValue();
        }

        int discount = 100 * (int)((dPrice-salePrice)/dPrice);
        target.getMerchantStock().add(new MerchantStock().quantity(source.getQuantity()).availability(source.getAvailability()).cost(cost).allow_backorder(false)
                .price(price).discount(discount).product(target));

    }

    @AfterMapping
    default void afterMapping(@MappingTarget ChildProduct target, Product source) {
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
        MerchantStock stock = source.getMerchantStock().stream().findFirst().orElse(null);
        if (stock != null) {
            target.setSalePriceObj(new Price(stock.getPrice(), source.getCurrency()));
            target.setPriceObj(new Price(source.getPrice(), source.getCurrency()));
            target.setDiscountInPercent(0);
            target.setCostObj(new Price(stock.getCost(), source.getCurrency()));
            target.setQuantity(stock.getQuantity());

            if(stock.getDiscount() != null) {
                int discount = stock.getDiscount();
                target.setDiscountInPercent(discount);
                //target.setPrice(new BigDecimal((int)(10*stock.getPrice().doubleValue()/(1.0-discount*.01))/10.0 ));
            }
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
