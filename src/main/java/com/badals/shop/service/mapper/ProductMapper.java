package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {


    @Mapping(target = "productLangs", ignore = true)
    @Mapping(target = "removeProductLang", ignore = true)
    @Mapping(source = "parent", target = "parentId")
    @Mapping(target = "price", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Mapping(source = "parent.id", target = "parent")
    @Mapping(source = "price.amount", target = "price", qualifiedByName = "doubleToString")
    @Mapping(source = "price.currency", target = "currency")
    ProductDTO toDto(Product product);

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
}
