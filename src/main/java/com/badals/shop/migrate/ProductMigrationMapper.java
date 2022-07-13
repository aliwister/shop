package com.badals.shop.migrate;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.enumeration.OrderChannel;
import com.badals.shop.domain.pojo.PriceMap;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantProduct;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link Order} and its DTO .
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProductMigrationMapper {

    @Mapping(target = "price", ignore = true)
    @Mapping(target = "children", ignore = true)
    TenantProduct toDto(Product orderDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantProduct target, Product source) {
        if(target.getMerchantId() == 1) {
            target.setApi("AZ");
        }
        PriceMap map = new PriceMap("OMR");
        if( source.getPrice() != null && source.getCurrency() != null) {
            map.getPrices().put(source.getCurrency(), source.getPrice().toString());
            target.setPrice(map);
        }
    }
}
