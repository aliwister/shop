package com.badals.shop.service.mapper;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Payment;
import com.badals.shop.domain.Product;
import com.badals.shop.service.dto.PaymentDTO;

import com.badals.shop.service.pojo.AddProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.reference", target = "orderReference")
    @Mapping(source = "order.cart.id", target = "cartId")
    PaymentDTO toDto(Payment payment);

    @AfterMapping
    default void afterMapping(@MappingTarget PaymentDTO target, Payment source) {
        if(source.getOrder() != null && source.getOrder().getCustomer() != null) {
            Customer c = source.getOrder().getCustomer();
            target.setCustomer(c.getFirstname() + " " + c.getLastname() + " / " + c.getId());
        }
    }

    @Mapping(source = "orderId", target = "order")
    Payment toEntity(PaymentDTO paymentDTO);

    default Payment fromId(Long id) {
        if (id == null) {
            return null;
        }
        Payment payment = new Payment();
        payment.setId(id);
        return payment;
    }
}
