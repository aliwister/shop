package com.badals.shop.service.mutation;

import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PaymentService;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/*
mutation {
  createOrder(id: 5) {
    id
  }
}

 */

@Component
public class AccountingMutation implements GraphQLMutationResolver {

    final private OrderService orderService;
    final private PurchaseService purchaseService;
    final private PaymentService paymentService;

    public AccountingMutation(OrderService orderService, PurchaseService purchaseService, PaymentService paymentService) {
        this.orderService = orderService;
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('ROLE_FINANCE')")
    public Message setSettlementDate(ArrayList<Long> paymentIds, Date date) {
        paymentService.setSettlementDate(paymentIds, date);
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_FINANCE')")
    public Message setProcessedDate(ArrayList<Long> paymentIds, Date date) {
        paymentService.setProcessedDate(paymentIds, date);
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_FINANCE')")
    public Message setAccountingCode(ArrayList<Long> paymentIds, String code) {
        paymentService.setAccountingCode(paymentIds, code);
        return new Message("Success");
    }

}