package com.badals.shop.graph.mutation;

import com.badals.shop.service.pojo.Message;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.util.MailService;
import com.badals.shop.service.TenantPaymentService;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.*;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/*
mutation {
  createOrder(id: 5) {
    id
  }
}

 */

@Component
public class OrderMutation implements GraphQLMutationResolver {

    final private PurchaseService purchaseService;
    final private TenantPaymentService paymentService;
    private final MailService mailService;

    public OrderMutation(PurchaseService purchaseService, TenantPaymentService paymentService, MailService mailService) {
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
        this.mailService = mailService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO createOrder(final Long id) {
        OrderDTO order = new OrderDTO();//this.orderService.createOrder(CartDTO cart);
        order.setId(id);
        return order;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PaymentDTO refundPayment(Long id, BigDecimal amount, String authCode, String bankName, String  bankAccountNumber, String  bankOwnerName, Long ref, String paymentMethod){
        PaymentDTO payment = paymentService.addRefund(id, amount, authCode, bankName, bankAccountNumber, bankOwnerName, ref, paymentMethod);
        return payment;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO discountOrder(Long id){
        return null;
    }
}

