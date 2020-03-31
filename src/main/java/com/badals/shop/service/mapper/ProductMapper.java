package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            target.setVariationOptions(source.getParent().getVariationOptions());
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
            target.setAvailability(processAvailability(hours, null).get("en"));
        }

        ProductLang lang = source.getProductLangs().stream().findFirst().orElse(null);
        if(lang != null) {
            target.setFeatures(lang.getFeatures());
            target.setDescription(lang.getDescription());
            target.setBrowseNode(lang.getBrowseNode());
        }
    }

    public static Map<String, String> processAvailability (int hours, String locale) {
        //messageSource.getMessage("pricing.request.success", null, LocaleContextHolder.getLocale());
        Map<String, String> availability = new HashMap<>();
        if(hours < 3) {
            //return "stock.availability.immediate";
            availability.put("en","Immediate");
            availability.put("ar","متوفر حالا");
        }
        else if(hours < 48) {
            availability.put("en", "1 to 2 days");
            availability.put("ar", "1 - 2 يوم");
        }
        else if(hours < 5*24) {
            availability.put("en", "5 to 7 days");
            availability.put("ar", "5 الى 7 أيام");
        }
        else if(hours < 8*24) {
            availability.put("en", "7 to 10 days");
            availability.put("ar", "7 الى 10 أيام");
        }
        else if(hours < 13*24) {
            availability.put("en", "10 to 14 days");
            availability.put("ar", "10 الى 14 يوم");
        }
        else if(hours < 20*24) {
            availability.put("en", "2 to 4 weeks");
            availability.put("ar", "2 - 4 أسابيع");
        }
        else if(hours < 31*24) {
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
