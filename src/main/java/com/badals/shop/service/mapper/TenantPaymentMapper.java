package com.badals.shop.service.mapper;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.tenant.TenantPayment;
import com.badals.shop.service.dto.PaymentDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {TenantOrderMapper.class})
public interface TenantPaymentMapper extends EntityMapper<PaymentDTO, TenantPayment> {

   @Mapping(source = "order.id", target = "orderId")
   @Mapping(source = "order.reference", target = "orderReference")
   @Mapping(source = "order.cart.id", target = "cartId")
   PaymentDTO toDto(TenantPayment payment);

   @AfterMapping
   default void afterMapping(@MappingTarget PaymentDTO target, TenantPayment source) {
      if(source.getOrder() != null && source.getOrder().getCustomer() != null) {
         Customer c = source.getOrder().getCustomer();
         target.setCustomer(c.getFirstname() + " " + c.getLastname() + " / " + c.getId());
      }
   }

   @Mapping(source = "orderId", target = "order")
   TenantPayment toEntity(PaymentDTO paymentDTO);

   default TenantPayment fromId(Long id) {
      if (id == null) {
         return null;
      }
      TenantPayment payment = new TenantPayment();
      payment.setId(id);
      return payment;
   }

}
