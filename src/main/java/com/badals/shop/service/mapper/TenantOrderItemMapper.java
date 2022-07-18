package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.service.dto.OrderItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity {@link TenantOrderItem} and its DTO {@link OrderItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {TenantOrderMapper.class, PurchaseItemMapper.class})
public interface TenantOrderItemMapper extends EntityMapper<OrderItemDTO, TenantOrderItem> {

    @Mapping(source = "order.id", target = "orderId")
/*    @Mapping(source = "product.url", target = "productUrl")*/
    @Mapping(source = "product.sku", target = "productSku")
    @Mapping(source = "product.ref", target = "productId")
    @Mapping(source = "product.merchantId", target = "productMerchantId")
    @Mapping(source = "purchaseItem.id", target = "purchaseItemId")
    @Mapping(source = "purchaseItem.purchase.id", target = "po")
    OrderItemDTO toDto(TenantOrderItem orderItem);

    @Mapping(source = "orderId", target = "order")
    TenantOrderItem toEntity(OrderItemDTO orderItemDTO);

    default TenantOrderItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantOrderItem orderItem = new TenantOrderItem();
        orderItem.setId(id);
        return orderItem;
    }
}
