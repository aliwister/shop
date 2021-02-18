package com.badals.shop.service.mapper;

import com.badals.shop.domain.Category;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
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
@Mapper(componentModel = "spring", uses = {ProductLangMapper.class, MerchantStockMapper.class, ProductLangMapper.class, ChildProductMapper.class})
public interface PartnerProductMapper extends EntityMapper<PartnerProduct, Product> {

    @Mapping(source="langs", target = "productLangs")
    @Mapping(source="options", target = "variationOptions")
    @Mapping(target = "removeProductLang", ignore = true)
    @Mapping(target = "merchantStock", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(target = "title", source = "name")
    @Mapping(target = "price", source = "salePrice")
    @Mapping(target = "dial", ignore = true)
    Product toEntity(PartnerProduct productDTO);

    @Mapping(target = "gallery", ignore = true)
    @Mapping(target = "merchant", ignore = true)
    @Mapping(source="dial.dial", target="dial")
    @Mapping(source="productLangs", target = "langs")
    @Mapping(source="variationOptions", target = "options")
    PartnerProduct toDto(Product product);

    @AfterMapping
    default void afterMapping(@MappingTarget Product target, PartnerProduct source) {
        if(source.getGallery() != null) {
            List<Gallery> gallery = new ArrayList<Gallery>();
            for(String g: source.getGallery()) {
                gallery.add(new Gallery(g));
            }
            target.setGallery(gallery);
        }

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

    @AfterMapping
    default void afterMapping(@MappingTarget ProductDTO target, AddProductDTO source) {
        ArrayList<Gallery> gallery = new ArrayList<Gallery>();
        if (source.getGallery() != null)
            for (String g : source.getGallery()) {
                gallery.add(new Gallery(g));
            }

        gallery.add(0, new Gallery(source.getImage()));
        target.setGallery(gallery);

        //int hours = stock.getAvailability();
        target.setAvailability(ProductMapper.processAvailability(target.getHours()).get("en"));

        if(source.getFeatures() != null)
            target.setFeatures(Arrays.asList(source.getFeatures().split(";")));

        target.setDescription(source.getDescription());
        target.setBrowseNode(source.getBrowseNode());

        if (source.getSalePrice() != null)
            target.setPrice(source.getSalePrice().setScale(2, RoundingMode.HALF_UP).toString());

        if(LocaleContextHolder.getLocale().toString().equals("ar")) {
            ///target.setLang("ar");
            target.setTitle(source.getName_ar());
            target.setBrowseNode(source.getBrowseNode_ar());
            target.setDescription(source.getDescription_ar());
            target.setBrand(source.getBrand_ar());
            if(source.getFeatures_ar() != null)
                target.setFeatures(Arrays.asList(source.getFeatures_ar().split(";")));
        }
    }

    @AfterMapping
    default void afterMapping(@MappingTarget PartnerProduct target, Product source) {
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
