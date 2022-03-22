package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.StockDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MerchantStock} and its DTO {@link StockDTO}.
 */
@Mapper(componentModel = "spring", uses = {MerchantMapper.class, ProductMapper.class})
public interface MerchantStockMapper extends EntityMapper<StockDTO, MerchantStock> {

    //@Mapping(source = "merchant.id", target = "merchantId")
    @Mapping(source = "product.id", target = "productId")
    StockDTO toDto(MerchantStock merchantStock);

    //@Mapping(source = "merchantId", target = "merchant")
    @Mapping(source = "productId", target = "product")
    MerchantStock toEntity(StockDTO stockDTO);

    default MerchantStock fromId(Long id) {
        if (id == null) {
            return null;
        }
        MerchantStock merchantStock = new MerchantStock();
        merchantStock.setId(id);
        return merchantStock;
    }
}
