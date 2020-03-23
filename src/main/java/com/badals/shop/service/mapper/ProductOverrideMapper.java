package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.ProductOverrideDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductOverride} and its DTO {@link ProductOverrideDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface ProductOverrideMapper extends EntityMapper<ProductOverrideDTO, ProductOverride> {


    ProductOverrideDTO toDto(ProductOverride productOverride);


    ProductOverride toEntity(ProductOverrideDTO productOverrideDTO);

    default ProductOverride fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductOverride productOverride = new ProductOverride();
        productOverride.setId(id);
        return productOverride;
    }
}
