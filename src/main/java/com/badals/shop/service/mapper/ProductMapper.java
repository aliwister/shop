package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;

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
    @Mapping(source = "price", target = "price", qualifiedByName = "doubleToString")
    @Mapping(source = "price", target = "salePrice", qualifiedByName = "doubleToString")
    @Mapping(source="title", target="title")
    @Mapping(source="title", target="description")
    @Mapping(target = "variations", ignore = true)
    @Mapping(target = "variationOptions", ignore = true)
    @Mapping(source = "ref", target = "id")
    ProductDTO toDto(Product product);


    @Named("mapWithoutCategories")
    @Mapping(source = "parent.id", target = "parent")
    @Mapping(source = "price", target = "price", qualifiedByName = "doubleToString")
    @Mapping(source = "price", target = "salePrice", qualifiedByName = "doubleToString")
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
        target.getGallery().add(0, new Gallery(source.getImage()));
    }
}
