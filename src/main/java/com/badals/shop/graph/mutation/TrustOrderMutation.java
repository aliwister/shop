package com.badals.shop.graph.mutation;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.TenantOrderService;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.service.util.MailService;
import com.badals.shop.service.TenantPaymentService;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.*;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;


/*
mutation {
  createOrder(id: 5) {
    id
  }
}

 */

@Component
public class TrustOrderMutation implements GraphQLMutationResolver {

    final private PurchaseService purchaseService;
    final private TenantOrderService orderService;
    final private TenantPaymentService paymentService;
    private final MailService mailService;

    public TrustOrderMutation(PurchaseService purchaseService, TenantOrderService orderService, TenantPaymentService paymentService, MailService mailService) {
        this.purchaseService = purchaseService;
        this.orderService = orderService;
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
    public PaymentDTO refundPayment(Long id, BigDecimal amount, String authCode, String bankName, String  bankAccountNumber, String  bankOwnerName, Long ref, String paymentMethod, String currency){
        PaymentDTO payment = paymentService.addRefund(id, amount, authCode, bankName, bankAccountNumber, bankOwnerName, ref, paymentMethod, currency);
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
    public PaymentDTO addPayment(Long orderId, BigDecimal amount, String paymentMethod, String authCode, String currency) {
        PaymentDTO payment = paymentService.addPayment(orderId, amount, paymentMethod, authCode, currency);

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
    public Message sendProductLevelEmail(Long orderId, List<Long> orderItems, String template) {
        if (template.equalsIgnoreCase("VOLTAGE")) {
            orderService.sendVoltageEmail(orderId, orderItems);
        }
        return new Message("Success");
    }
}

