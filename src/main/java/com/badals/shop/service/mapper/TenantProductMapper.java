package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.service.CurrencyService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.StockDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.badals.shop.service.CurrencyService.BASE_CURRENCY_KEY;

/**
 * Mapper for the entity {@link TenantProduct} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class})
public interface TenantProductMapper extends EntityMapper<ProductDTO, TenantProduct> {


    @Mapping(source = "parent.id", target = "parent")
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(source = "ref", target = "id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "listPrice", source="listPrice", qualifiedByName = "withCurrencyConversionMap")
    @Mapping(target = "price", source="price", qualifiedByName = "withCurrencyConversionMap")
    @Mapping(target = "stock", source="stock", qualifiedByName = "withCountStock")
    @Mapping(target = "merchantStock", source="stock", qualifiedByName = "withStock")
    ProductDTO toDto(TenantProduct product);

    @Named("mapWithBaseCurrency")
    @Mapping(source = "parent.id", target = "parent")
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(source = "ref", target = "id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "listPrice", source="listPrice", qualifiedByName = "withBaseCurrencyMap")
    @Mapping(target = "price", source="price", qualifiedByName = "withBaseCurrencyMap")
    @Mapping(target = "stock", source="stock", qualifiedByName = "withCountStock")
    ProductDTO toPartnerListDto(TenantProduct product);

    @Mapping(target = "listPrice", ignore = true)
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "stock", ignore = true)
    TenantProduct toEntity(ProductDTO product);

    @Named("withCurrencyConversionMap")
    public static String withCurrencyConversionMap(PriceMap priceMap) {
        Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();
        if (priceMap == null)
            return null;
        String price = priceMap.getPriceForCurrency(targetCurrency);
        if(price == null) {
            String baseCurrency = priceMap.getBase();
            price = priceMap.getPriceForCurrency(baseCurrency);
            price = CurrencyService.convert(price, baseCurrency, targetCurrency);
        }
        return price;
    }
    @Named("withCountStock")
    public static String withCountStock(Set<TenantStock> stock) {
        if(stock == null || stock.isEmpty())
            return "0";

        BigDecimal value = stock.stream().map(x -> x.getQuantity()).reduce(BigDecimal.ZERO, (a, b) -> a.add(b));
        return value.toString();
    }

    @Named("withStock")
    public static List<StockDTO> withStock(Set<TenantStock> stock) {
        if(stock == null || stock.isEmpty())
            return new ArrayList<>();
        // todo check if price and cost are same in StockDTO
        return stock.stream().map(x -> new StockDTO(x.getId(),x.getQuantity(),
            x.getAvailability(),x.isAllow_backorder(),
            x.getBackorder_availability(), "", x.getLocation(),
            x.getStore(),
            x.getCost() != null ? x.getCost().toString() : "",
            x.getCost() != null ? x.getCost().toString() : "",
            x.getProduct().getMerchantId(),
            x.getProduct().getId())).collect(Collectors.toList());
    }

    @Named("withBaseCurrencyMap")
    public static String withBaseCurrencyMap(PriceMap priceMap) {
        String baseCurrency = priceMap.getBase();
        String price = priceMap.getPriceForCurrency(baseCurrency);
        return price;
    }


    default TenantProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantProduct product = new TenantProduct();
        product.setId(id);
        return product;
    }

    @Named("doubleToString")
    public static String doubleToString(double amount) {
        return String.valueOf(amount);
    }

    @AfterMapping
    default void afterMapping(@MappingTarget ProductDTO target, TenantProduct source) {
        Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();
        String langCode = locale.getLanguage();
        target.setCurrency(targetCurrency);
        target.setInStock(true);

        TenantProductLang lang = Objects.requireNonNullElse(source.getLangs(),new ArrayList<TenantProductLang>()).stream().filter(x-> x!= null && x.getLang().equals(langCode)).findFirst().orElse(null);

        if(lang == null) {
            lang =  Objects.requireNonNullElse(source.getLangs(),new ArrayList<TenantProductLang>()).stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(null);
        }

        TenantProductLang parentLang = lang;

        if(source.getVariationType() == VariationType.CHILD) {
            parentLang = lang;
            if(source.getParent() != null && source.getParent().getLangs() != null)
                parentLang = source.getParent().getLangs().stream().filter(x-> x!= null && x.getLang().equals(langCode)).findFirst().orElse(lang);
        }

        if(lang == null && !langCode.equals("en")) {
            lang =  Objects.requireNonNullElse(source.getLangs(),new ArrayList<TenantProductLang>()).stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(null);
            parentLang = lang;
            if(source.getVariationType() == VariationType.CHILD)
                parentLang = source.getParent().getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(lang);
        }

        if(lang != null ) {
            target.setTitle(Objects.nonNull(lang.getTitle()) ? lang.getTitle() : source.getTitle());
            target.setFeatures(parentLang.getFeatures());
            target.setDescription(parentLang.getDescription());
        }
    }

    @AfterMapping
    default void afterMapping(@MappingTarget TenantProduct target, ProductDTO source) {

        target.setPrice(new PriceMap(source.getCurrency()));
        target.getPrice().push(source.getCurrency(), new BigDecimal(source.getPrice()));
        target.setListPrice(new PriceMap(source.getCurrency()));
        if(source.getListPrice() != null)
            target.getListPrice().push(source.getCurrency(), new BigDecimal(source.getListPrice()));
        TenantProductLang lang = new TenantProductLang();
        lang.setLang("en");
        lang.setDescription(source.getDescription());
        lang.setTitle(source.getTitle());

        target.getLangs().add(lang);
        target.setVariationType(source.getVariationType());

 /*       Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();
        String langCode = locale.getLanguage();
        target.setCurrency(targetCurrency);
        target.setInStock(true);


        if(lang == null) {
            lang = source.getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().get();
        }

        TenantProductLang parentLang = lang;

        if(source.getVariationType() == VariationType.CHILD) {
            parentLang = lang;
            if(source.getParent().getLangs() != null)
                parentLang = source.getParent().getLangs().stream().filter(x-> x!= null && x.getLang().equals(langCode)).findFirst().orElse(lang);
        }

        if(lang == null && !langCode.equals("en")) {
            lang = source.getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(null);
            parentLang = lang;
            if(source.getVariationType() == VariationType.CHILD)
                parentLang = source.getParent().getLangs().stream().filter(x-> x!= null && x.getLang().equals("en")).findFirst().orElse(lang);
        }

        if(lang != null) {
            target.setTitle(lang.getTitle());
            target.setFeatures(parentLang.getFeatures());
            target.setDescription(parentLang.getDescription());
        }*/
    }

    @BeforeMapping
    default void beforeMapping(@MappingTarget ProductDTO target, TenantProduct source) {
        if (source.getParent() != null) {
            target.setVariations(source.getParent().getVariations());
            List<String> dimensions = source.getVariationAttributes().stream().map(x -> x.getName()).collect(Collectors.toList());
            List<VariationOption> variationOptions = new ArrayList<>();
            for(String dim: dimensions) {
                VariationOption o = new VariationOption();
                o.setLabel(dim);
                o.setName(dim);
                o.setValues(new ArrayList<>());
                variationOptions.add(o);
                //List<String> values = source.getParent().getVariations().stream().filter(x -> (x.getVariationAttributes().stream().filter(y -> y.getName().toLowerCase().startsWith(dim.toLowerCase())).findFirst().get())
            }
            // Generate variation list on the fly

            for (Variation v : Objects.requireNonNullElse(source.getParent().getVariations(), new ArrayList<Variation>())) {
                for(Attribute attribute: v.getVariationAttributes()) {
                    try {
                        variationOptions.stream().filter(y -> attribute.getName().toLowerCase().startsWith(y.getName().toLowerCase())).findFirst().get().getValues().add(attribute.getValue());
                    }
                    catch (NoSuchElementException e) {
                        target.setStub(true);
                    }
                }

            }
            variationOptions.forEach(o -> o.setValues(new ArrayList<>(new HashSet<>(o.getValues()))));
            target.setVariationOptions(variationOptions);
        }
        else {
            target.setVariations(source.getVariations());
            target.setVariationOptions(source.getVariationOptions());
        }
    }

    public static Map<String, String> processAvailability (int hours) {
        //messageSource.getMessage("pricing.request.success", null, LocaleContextHolder.getLocale());
        Map<String, String> availability = new HashMap<>();
        if(hours < 3) {
            //return "stock.availability.immediate";
            availability.put("en","Immediate");
            availability.put("ar","متوفر حالا");
        }
        else if(hours <= 48) {
            availability.put("en", "2 - 3 Days");
            availability.put("ar", "خلال يومين الى 3");
        }
        else if(hours < 5*24) {
            availability.put("en", "5 to 7 working days");
            availability.put("ar", "5 الى 7 أيام عمل");
        }
        else if(hours < 8*24) {
            availability.put("en", "7 to 10 working days");
            availability.put("ar", "7 الى 10 أيام عمل");
        }
        else if(hours < 8*24) {
            availability.put("en", "2 to 3 weeks");
            availability.put("ar", "2 - 3 أسابيع");
        }
        else if(hours < 13*24) {
            availability.put("en", "3 to 5 weeks");
            availability.put("ar", "3 - 5 أسابيع");
        }
        else if(hours < 16*24) {
            availability.put("en", "1 to 2 months");
            availability.put("ar", "1 - 2 شهر");
        }
        else if(hours < 60*24) {
            availability.put("en", "2 to 3 months");
            availability.put("ar", "2 - 3 أشهر");
        }
        return availability;
    }
}
