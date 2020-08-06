package com.badals.shop.service.mutation;

import com.badals.shop.service.OrderService;
import com.badals.shop.service.PaymentService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.dto.PaymentDTO;
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
public class OrderMutation implements GraphQLMutationResolver {

    final private OrderService orderService;
    final private PaymentService paymentService;

    public OrderMutation(OrderService orderService, PaymentService paymentService) {
        this.orderService = orderService;
        this.paymentService = paymentService;
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
        OrderDTO order = orderService.cancelOrder(id, reason);
        return order;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PaymentDTO refundPayment(Long id, BigDecimal amount, String authCode, String bankName, String  bankAccountNumber, String  bankOwnerName, Long ref, String paymentMethod){
        PaymentDTO payment = paymentService.addRefund(id, amount, authCode, bankName, bankAccountNumber, bankOwnerName, ref, paymentMethod);
        return payment;
    }
}

