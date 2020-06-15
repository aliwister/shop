package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.OrderItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class, PurchaseItemMapper.class})
public interface OrderItemMapper extends EntityMapper<OrderItemDTO, OrderItem> {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "product.url", target = "productUrl")
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "product.ref", target = "productId")
    @Mapping(source = "purchaseItem.id", target = "purchaseItemId")
    @Mapping(source = "purchaseItem.purchase.id", target = "po")
    OrderItemDTO toDto(OrderItem orderItem);

    @Mapping(source = "orderId", target = "order")
    OrderItem toEntity(OrderItemDTO orderItemDTO);

    default OrderItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderItem orderItem = new OrderItem();
        orderItem.setId(id);
        return orderItem;
    }
}
