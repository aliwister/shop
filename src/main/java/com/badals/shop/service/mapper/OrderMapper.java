package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;

import java.math.BigDecimal;

/**
 * Mapper for the entity {@link Order} and its DTO {@link OrderDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class, AddressMapper.class, OrderItemMapper.class, PaymentMapper.class})
public interface OrderMapper extends EntityMapper<OrderDTO, Order> {

    @Mapping(target = "orderItems", ignore = true)
    @Mapping(target = "removeOrderItem", ignore = true)
    Order toEntity(OrderDTO orderDTO);

    @Mapping(target="cart", ignore = true)
    @Mapping(source = "cart.id", target="cartId")
    OrderDTO toDto(Order order);

    default Order fromId(Long id) {
        if (id == null) {
            return null;
        }
        Order order = new Order();
        order.setId(id);
        return order;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget OrderDTO target, Order source) {
        if(target.getDeliveryAddress() ==  null)
            target.setDeliveryAddress(AddressDTO.fromAddressPojo(source.getDeliveryAddressPojo()));
        BigDecimal balance = target.getTotal();
        if(target.getPayments() != null) {
            for (PaymentDTO p: target.getPayments())
                balance = balance.subtract(p.getAmount());
            target.setBalance(balance);
        }

    }
}
