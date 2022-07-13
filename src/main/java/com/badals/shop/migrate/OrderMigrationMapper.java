package com.badals.shop.migrate;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderChannel;
import com.badals.shop.domain.pojo.OrderAdjustment;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.service.mapper.EntityMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Mapper for the entity {@link Order} and its DTO .
 */
@Mapper(componentModel = "spring", uses = {OrderItemMigrationMapper.class, PaymentMigrationMapper.class})
public interface OrderMigrationMapper {

    @Mapping(target = "cart", ignore = true)
    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "payments", ignore = true)
    TenantOrder toDto(Order orderDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantOrder target, Order source) {
        target.setChannel(OrderChannel.WEB);
        List<OrderAdjustment> adjustments = new ArrayList<>();
        if(source.getDiscountsTotal() != null && !source.getDiscountsTotal().equals(BigDecimal.ZERO)) {
            adjustments.add(OrderAdjustment.discount(source.getCouponName(), source.getDiscountsTotal()));
        }
        if(source.getDeliveryTotal() != null && !source.getDeliveryTotal().equals(BigDecimal.ZERO)) {
            adjustments.add(OrderAdjustment.delivery(source.getCarrier(), source.getDeliveryTotal()));
        }
        target.setOrderAdjustments(adjustments);
    }
}
