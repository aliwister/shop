package com.badals.shop.service.mapper;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.AlgoliaProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface AlgoliaProductMapper {


    @Mapping(target = "price", ignore = true)
    @Mapping(source = "parent.id", target = "parent")
    AlgoliaProduct producttoAlgoliaProduct(Product product);
}
