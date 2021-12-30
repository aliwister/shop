package com.badals.shop.graph.query;


import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO orderA(Long id) throws OrderNotFoundException {
        OrderDTO o = orderService.getOrderWithOrderItems(id).orElse(null);
        if(o == null) throw new OrderNotFoundException("No order found with this name");
        return o;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderResponse ordersA(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance) throws OrderNotFoundException {
        return  orderService.getOrders(orderState, offset, limit, searchText, balance);
        //return orders;
    }


}
