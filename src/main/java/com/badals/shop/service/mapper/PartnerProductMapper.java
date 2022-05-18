package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.service.CurrencyService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.PartnerProduct;
import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductLangMapper.class, MerchantStockMapper.class, ProductLangMapper.class, ChildProductMapper.class})
public interface PartnerProductMapper extends EntityMapper<PartnerProduct, TenantProduct> {

    //@Mapping(source="langs", target = "productLangs")
    @Mapping(source="options", target = "variationOptions")
    //@Mapping(target = "removeProductLang", ignore = true)
    //@Mapping(target = "merchantStock", ignore = true)
    @Mapping(target = "gallery", ignore = true)
    ////@Mapping(target = "merchant", ignore = true)
    //@Mapping(target = "children", ignore = true)
    @Mapping(target = "price", source = "price", qualifiedByName = "pricelistToMap")
    @Mapping(target = "listPrice", source = "listPrice", qualifiedByName = "pricelistToMap")
    //@Mapping(target = "currency", source = "priceObj.currency")
    //@Mapping(target = "dial", ignore = true)
    TenantProduct toEntity(PartnerProduct productDTO);

    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(source = "ref", target = "id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "listPrice", source="listPrice", qualifiedByName = "withCurrencyConversionList")
    @Mapping(target = "price", source="price", qualifiedByName = "withCurrencyConversionList")
    @Mapping(target = "gallery", source="gallery", qualifiedByName = "buildGallery")
    ProductDTO toProductDto(PartnerProduct product);

    @Mapping(target = "gallery", ignore = true)
    //@Mapping(target = "merchant", ignore = true)
    //@Mapping(source="dial.dial", target="dial")
    //@Mapping(source="productLangs", target = "langs")
    @Mapping(source="variationOptions", target = "options")
    @Mapping(target = "listPrice", source="listPrice", qualifiedByName = "priceMapToList")
    @Mapping(target = "price", source="price", qualifiedByName = "priceMapToList")
    //@Mapping(target = "tenant", source="tenantId")
    PartnerProduct toDto(TenantProduct product);


    @Named("pricelistToMap")
    public static PriceMap pricelistToMap(PriceList list) {
        PriceMap priceMap = new PriceMap();
        list.getPriceList().stream().forEach(x -> priceMap.push(x.getCurrency(), x.getAmount()));
        priceMap.setBase(list.getBaseCurrency());
        if(list.getBaseCurrency() == null && !list.getPriceList().isEmpty())
            priceMap.setBase(list.getPriceList().get(0).getCurrency());
        return priceMap;
    }
    @Named("buildGallery")
    public static List<Gallery> gallery(List<String> gallery) {
        return gallery.stream().map(x -> new Gallery(x)).collect(Collectors.toList());
    }

    @Named("priceMapToList")
    public static PriceList priceMapToList(PriceMap map) {
        PriceList priceList = new PriceList();
        map.getPrices().keySet().stream().forEach(x -> priceList.getPriceList().add(new Price(new BigDecimal(map.getPriceForCurrency(x)), x)));
        priceList.setBaseCurrency(map.getBase());
        return priceList;
    }


    @Named("withCurrencyConversionList")
    public static String withCurrencyConversionList(PriceList list) {
        Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();
        String price = list.getPriceForCurrency(targetCurrency);
        if(price == null) {
            String baseCurrency = list.getBaseCurrency();
            price = list.getPriceForCurrency(baseCurrency);
            if (price == null) return "";
            price = CurrencyService.convert(price.toString(), baseCurrency, targetCurrency);
        }
        return price;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget TenantProduct target, PartnerProduct source) {
        if(source.getGallery() != null) {
            List<Gallery> gallery = new ArrayList<Gallery>();
            for(String g: source.getGallery()) {
                gallery.add(new Gallery(g));
            }
            target.setGallery(gallery);
        }

        if(source.getVariationType() == null)
            target.setVariationType(VariationType.SIMPLE);
        else
            target.setVariationType(VariationType.valueOf(source.getVariationType()));


        //target(name);
/*
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
    default void afterMapping(@MappingTarget PartnerProduct target, TenantProduct source) {
        String langCode = "en";
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
/*            target.setSalePriceObj(stock.getPrice());
            target.setPriceObj(source.getPrice());*/
            target.setCost(stock.getCost());
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
        //String name = source.getLangs().stream().filter(x -> x != null && x.getLang().equals("en")).findFirst().get().getName();
        //target.setTi(name);
    }

    @AfterMapping
    default void afterMapping(@MappingTarget ProductDTO target, PartnerProduct source) {
        Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();
        String langCode = locale.getLanguage();
        target.setCurrency(targetCurrency);
        target.setInStock(true);

        TenantProductLang lang = source.getLangs().stream().filter(x-> x!= null && x.getLang().equals(langCode)).findFirst().orElse(null);

        if(lang == null) {
            lang = source.getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().get();
        }

        TenantProductLang parentLang = lang;

/*        if(source.getVariationType() == VariationType.CHILD) {
            parentLang = lang;
            if(source.getParent().getLangs() != null)
                parentLang = source.getParent().getLangs().stream().filter(x-> x!= null && x.getLang().equals(langCode)).findFirst().orElse(lang);
        }

        if(lang == null && !langCode.equals("en")) {
            lang = source.getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(null);
            parentLang = lang;
            if(source.getVariationType() == VariationType.CHILD)
                parentLang = source.getParent().getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(lang);
        }*/

        if(lang != null) {
            target.setTitle(lang.getTitle());
            target.setFeatures(parentLang.getFeatures());
            target.setDescription(parentLang.getDescription());
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
