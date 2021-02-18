package com.badals.shop.service.mapper;

import com.badals.shop.domain.Category;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.ChildProduct;
import com.badals.shop.service.pojo.PartnerProduct;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    @Mapping(target = "price", source = "salePrice")
    @Mapping(target = "dial", ignore = true)
    Product toEntity(ChildProduct productDTO);

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

        int discount = 100 * (int)((source.getPrice().doubleValue() - source.getSalePrice().doubleValue())/source.getPrice().doubleValue());
        target.getMerchantStock().add(new MerchantStock().quantity(source.getQuantity()).availability(source.getAvailability()).cost(source.getCost()).allow_backorder(false)
                .price(source.getSalePrice()).discount(discount));
/*        if(source.getType() == null)
            target.setVariationType(VariationType.SIMPLE);
        else
            target.setVariationType(VariationType.valueOf(source.getType()));

        ProductLang langAr = new ProductLang().lang("ar").description(source.getDescription_ar()).title(source.getName_ar()).brand(source.getBrand_ar()).browseNode(source.getBrowseNode());
        if(source.getFeatures_ar() != null)
            langAr.setFeatures(Arrays.asList(source.getFeatures_ar().split(";")));

        ProductLang langEn = new ProductLang().lang("en").description(source.getDescription()).title(source.getName()).brand(source.getBrand()).browseNode(source.getBrowseNode());
        if(source.getFeatures() != null)
            langEn.setFeatures(Arrays.asList(source.getFeatures().split(";")));
        target.getProductLangs().add(langAr.product(target));
        target.getProductLangs().add(langEn.product(target));

        target.setActive(true);
        target.getMerchantStock().clear();
        target.getMerchantStock().add(new MerchantStock().quantity(source.getQuantity()).availability(source.getAvailability()).cost(source.getCost()).allow_backorder(false)
                .price(source.getSalePrice()).discount(source.getDiscountInPercent()).product(target).merchantId(source.getMerchantId()));*/

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
