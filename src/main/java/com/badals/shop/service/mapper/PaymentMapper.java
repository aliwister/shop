package com.badals.shop.service.mapper;

import com.badals.shop.domain.Payment;
import com.badals.shop.service.dto.PaymentDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Payment} and its DTO {@link PaymentDTO}.
 */
@Mapper(componentModel = "spring", uses = {OrderMapper.class})
public interface PaymentMapper extends EntityMapper<PaymentDTO, Payment> {

    @Mapping(source = "order.id", target = "orderId")
    @Mapping(source = "order.reference", target = "orderReference")
    PaymentDTO toDto(Payment payment);

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
