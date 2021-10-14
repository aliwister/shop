package com.badals.shop.service.mapper;

import com.badals.shop.aop.logging.LocaleContext;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.ProfileProduct;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;

import java.util.*;
import java.util.stream.Collectors;

import static com.badals.shop.service.util.AccessUtil.opt;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, MerchantStockMapper.class})
public interface ProfileProductMapper extends EntityMapper<ProductDTO, ProfileProduct> {


    @Mapping(source = "parent.id", target = "parent")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "salePrice", ignore = true)
    @Mapping(target = "title", ignore = true)
    @Mapping(target = "description", ignore = true)
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(source = "ref", target = "id")
    @Mapping(target = "categories", ignore = true)
    ProductDTO toDto(ProfileProduct product);

    @Mapping(target = "price", ignore = true)
    ProfileProduct toEntity(ProductDTO product);


    default ProfileProduct fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileProduct product = new ProfileProduct();
        product.setId(id);
        return product;
    }

    @Named("doubleToString")
    public static String doubleToString(double amount) {
        return String.valueOf(amount);
    }

    @AfterMapping
    default void afterMapping(@MappingTarget ProductDTO target, ProfileProduct source) {
        target.setCurrency(source.getPrice().getCurrency());
        target.setPrice(source.getPrice().getAmount().toPlainString());
        target.setInStock(true);

        String langCode = LocaleContext.getLocale().substring(0,2);
        String currency = LocaleContext.getLocale().substring(3);

        TenantProductLang lang = source.getLangs().stream().filter(x-> x!= null && x.getLang().equals(langCode)).findFirst().orElse(null);

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
            target.setTitle(lang.getName());
            target.setFeatures(parentLang.getFeatures());
            target.setDescription(parentLang.getDescription());
        }
    }

    @BeforeMapping
    default void beforeMapping(@MappingTarget ProductDTO target, ProfileProduct source) {
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

            for (Variation v : source.getParent().getVariations()) {
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
