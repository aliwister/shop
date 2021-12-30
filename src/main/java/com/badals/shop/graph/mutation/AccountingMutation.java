package com.badals.shop.graph.mutation;

import com.badals.shop.service.pojo.Message;
import com.badals.shop.service.CheckoutComService;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PaymentService;
import com.badals.shop.service.PurchaseService;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;


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
    final private CheckoutComService checkoutComService;

    public AccountingMutation(OrderService orderService, PurchaseService purchaseService, PaymentService paymentService, CheckoutComService checkoutComService) {
        this.orderService = orderService;
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
        this.checkoutComService = checkoutComService;
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
    //@PreAuthorize("hasRole('ROLE_CHECKOUT_REFUND')")
    public Message processCheckoutRefund(String token, String amount, String ref, String description) throws Exception {
        return checkoutComService.refund(token, amount, ref, description);
    }
}