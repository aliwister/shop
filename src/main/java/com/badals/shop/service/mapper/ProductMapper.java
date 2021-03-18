package com.badals.shop.service.mapper;

import com.amazon.paapi5.v1.VariationAttribute;
import com.badals.shop.domain.*;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.badals.shop.service.util.AccessUtil.opt;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {CategoryMapper.class, MerchantStockMapper.class})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {


    @Mapping(target = "productLangs", ignore = true)
    @Mapping(target = "removeProductLang", ignore = true)
    @Mapping(target = "merchantStock", ignore = true)
    @Mapping(source = "parent", target = "parentId")
    @Mapping(target = "dial", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(source = "parent.id", target = "parent")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "salePrice", ignore = true)
    @Mapping(source="title", target="title")
    @Mapping(source="title", target="description")
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(source = "ref", target = "id")
    @Mapping(target = "categories", ignore = true)
    @Mapping(source="dial.dial", target="dial")
    ProductDTO toDto(Product product);

    @Named("mapWithoutCategories")
    @Mapping(source = "parent.id", target = "parent")
    //@Mapping(source = "price", target = "price", qualifiedByName = "doubleToString")
    //@Mapping(source = "price", target = "salePrice", qualifiedByName = "doubleToString")
    @Mapping(target = "price", ignore = true)
    @Mapping(target = "salePrice", ignore = true)
    @Mapping(source="title", target="title")
    @Mapping(source="title", target="description")
    @Mapping(source = "currency", target = "currency")
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(target = "dial", ignore = true)
    ProductDTO toDtoWOCategories(Product product);

    default Product fromId(Long id) {
        if (id == null) {
            return null;
        }
        Product product = new Product();
        product.setId(id);
        return product;
    }

    @Named("doubleToString")
    public static String doubleToString(double amount) {
        return String.valueOf(amount);
    }

    @BeforeMapping
    default void beforeMapping(@MappingTarget ProductDTO target, Product source) {
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
                    variationOptions.stream().filter(y -> attribute.getName().toLowerCase().startsWith(y.getName().toLowerCase())).findFirst().get().getValues().add(attribute.getValue());
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
    @AfterMapping
    default void afterMapping(@MappingTarget ProductDTO target, Product source) {
        if (target.getGallery() == null) {
            target.setGallery(new ArrayList<Gallery>());
        }
        target.getGallery().add(0, new Gallery(source.getImage()));

        // Process sale price and discount percentage
        MerchantStock stock = source.getMerchantStock().stream().findFirst().orElse(null);
        if (stock != null) {
            target.setSalePrice(stock.getPrice().toPlainString());
            target.setPrice(stock.getPrice().toPlainString());
            target.setDiscountInPercent(0);

            if(stock.getDiscount() != null) {
                int discount = stock.getDiscount();
                target.setDiscountInPercent(discount);
                target.setPrice(String.valueOf((int)(10*stock.getPrice().doubleValue()/(1.0-discount*.01))/10.0 ));
            }
            int hours = stock.getAvailability();
            /*
            @Todo
            Move to language files
             */
            //System.out.println("LOCALE:"+LocaleContextHolder.getLocale());
            Map<String, String> a = processAvailability(hours);
            target.setAvailability(a.get(LocaleContextHolder.getLocale().toString()));
        }

        ProductLang lang = source.getProductLangs().stream().findFirst().orElse(null);
        if(lang != null) {
            target.setTitle(lang.getTitle());
            target.setFeatures(lang.getFeatures());
            target.setDescription(lang.getDescription());
            target.setBrowseNode(lang.getBrowseNode());
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
            availability.put("en", "2-3 Days");
            availability.put("ar", "خلال يومين الى 3");
        }
        /* else if(hours < 5*24) {
            availability.put("en", "5 to 7 days");
            availability.put("ar", "5 الى 7 أيام");
        }
        else if(hours < 8*24) {
            availability.put("en", "10 to 10 days");
            availability.put("ar", "7 الى 10 أيام");
        }*/
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
            availability.put("ar", "2 - 3 شهور");
        }
        return availability;
    }
}
