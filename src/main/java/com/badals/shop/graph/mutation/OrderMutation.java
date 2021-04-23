package com.badals.shop.graph.mutation;

import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.MailService;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PaymentService;
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

    final private OrderService orderService;
    final private PurchaseService purchaseService;
    final private PaymentService paymentService;
    private final MailService mailService;

    public OrderMutation(OrderService orderService, PurchaseService purchaseService, PaymentService paymentService, MailService mailService) {
        this.orderService = orderService;
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
    public OrderDTO editOrder(Long id, List<OrderItemDTO> orderItems, String reason) {
        OrderDTO order = orderService.editOrderItems(id, orderItems, reason);
        return order;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO cancelOrder(Long id, String reason){
        OrderDTO order = orderService.cancel(id, reason);
        return order;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO closeOrder(Long id, String reason){
        OrderDTO order = orderService.close(id, reason);
        return order;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PaymentDTO refundPayment(Long id, BigDecimal amount, String authCode, String bankName, String  bankAccountNumber, String  bankOwnerName, Long ref, String paymentMethod){
        PaymentDTO payment = paymentService.addRefund(id, amount, authCode, bankName, bankAccountNumber, bankOwnerName, ref, paymentMethod);
        return payment;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendPaymentSms(Long id, String mobile) throws Exception {
        orderService.sendRequestPaymentSms(id, mobile);
        return new Message("done");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO discountOrder(Long id){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO setOrderState(Long id, OrderState state){
        return orderService.setStatus(id, state);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendOrderLevelEmail(Long id, String template) {

        if(template.equalsIgnoreCase("NEW_ORDER")) {
            orderService.sendConfirmationEmail(id);
        }
        else if(template.equalsIgnoreCase("NEW_PAYMENT")) {
            PaymentDTO payment = paymentService.findOne(id).orElse(null);
            OrderDTO order = orderService.getOrderWithOrderItems(payment.getOrderId()).orElse(null);
            CustomerDTO customer = order.getCustomer();
            mailService.sendPaymentAddedMail(customer, payment);
        }

        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PaymentDTO addPayment(Long orderId, BigDecimal amount, String paymentMethod, String authCode) {
        PaymentDTO payment = paymentService.addPayment(orderId, amount, paymentMethod, authCode);

        OrderDTO order = orderService.setOrderState(orderId, OrderState.PAYMENT_ACCEPTED);
        // Send Email
        payment = paymentService.findOne(payment.getId()).orElse(null);
        CustomerDTO customer = order.getCustomer();
        mailService.sendPaymentAddedMail(customer, payment);
        // Return
        return payment;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message addDiscount(Long orderId, BigDecimal amount, String couponName) {
        OrderDTO order = orderService.addDiscount(orderId, amount, couponName);
        // Return
        return new Message("Discount Added successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message contact(final Long id) {
        orderService.sendPaymnetMessage(id);
        return new Message("SMS Sent successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendProductLevelEmail(Long orderId, ArrayList<Long> orderItems, String template) {
        if (template.equalsIgnoreCase("VOLTAGE")) {
            orderService.sendVoltageEmail(orderId, orderItems);
        }
        return new Message("Success");
    }

}

