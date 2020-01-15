package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.ProductLangDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ProductLang} and its DTO {@link ProductLangDTO}.
 */
@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface ProductLangMapper extends EntityMapper<ProductLangDTO, ProductLang> {

    @Mapping(source = "product.id", target = "productId")
    ProductLangDTO toDto(ProductLang productLang);

    @Mapping(source = "productId", target = "product")
    ProductLang toEntity(ProductLangDTO productLangDTO);

    default ProductLang fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProductLang productLang = new ProductLang();
        productLang.setId(id);
        return productLang;
    }
}
