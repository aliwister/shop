package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import org.mapstruct.*;

import java.math.BigDecimal;

/**
 * Mapper for the entity {@link TenantOrder} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, AddressMapper.class, TenantOrderItemMapper.class})
public interface TenantOrderMapper extends EntityMapper<OrderDTO, TenantOrder> {

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "removeOrderItem", ignore = true)
    TenantOrder toEntity(OrderDTO orderDTO);

    @Mapping(target="cart", ignore = true)
    @Mapping(source = "cart.id", target="cartId")
    @Mapping(source = "cart.secureKey", target="cartSecureKey")
    @Mapping(source = "orderItems", target="items")
    OrderDTO toDto(TenantOrder order);

    @Mapping(target="cart", ignore = true)
    @Mapping(target="customer", ignore = true)
    @Mapping(source = "cart.id", target="cartId")
    @Mapping(source = "cart.secureKey", target="cartSecureKey")
    @Mapping(source = "orderItems", target="items")
    @Mapping(source = "customer.id", target="customerId")
    @Named("toIndexDto")
    OrderDTO toIndexDto(TenantOrder order);

    default TenantOrder fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantOrder order = new TenantOrder();
        order.setId(id);
        return order;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget OrderDTO target, TenantOrder source) {
        if(target.getDeliveryAddress() ==  null)
            target.setDeliveryAddress(AddressDTO.fromAddressPojo(source.getDeliveryAddressPojo()));
        BigDecimal balance = target.getTotal();
        if(target.getPayments() != null) {
            for (PaymentDTO p: target.getPayments())
                if(p.getVoided()!= null && !p.getVoided())
                    balance = balance.subtract(p.getAmount());
            target.setBalance(balance);
        }

    }
}
