package com.badals.shop.graph.query;

import com.badals.shop.graph.OrderResponse;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PaymentService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    OrderService orderService;

    @Autowired
    PaymentService paymentService;


    @Autowired
    private ProductService productService;

    public OrderDTO orderConfirmation(String ref, String key) throws OrderNotFoundException {

        return orderService.getOrderConfirmation(ref,key);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public OrderResponse orders(int limit, int offset) throws OrderNotFoundException {
        OrderResponse orders = orderService.getCustomerOrders(limit, offset);

        return orders;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PaymentDTO> payments(long orderId) {
        return paymentService.findForOrder(orderId);
    }
    //@PreAuthorize("hasRole('ROLE_ADMIN')")


    //public String getCheckout()
}

