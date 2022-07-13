package com.badals.shop.migrate;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.OrderItem;
import com.badals.shop.domain.enumeration.OrderChannel;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.service.mapper.EntityMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link OrderItem} and its DTO.
 */
@Mapper(componentModel = "spring", uses = {OrderMigrationMapper.class, ProductMigrationMapper.class})
public interface OrderItemMigrationMapper {

    //@Mapping(target = "product", ignore = true)
    @Mapping(target = "order", ignore = true)
    TenantOrderItem toDto(OrderItem orderItem);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantOrderItem target, OrderItem source) {
        //target.setChannel(OrderChannel.WEB);
        //target.setTenantId("badals");
    }

}
