package com.badals.shop.service.query;


import com.badals.shop.domain.PricingRequest;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class OrderAdminQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    private OrderService orderService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO orderA(Long id) throws OrderNotFoundException {
        OrderDTO o = orderService.getOrderWithOrderItems(id).orElse(null);
        if(o == null) throw new OrderNotFoundException("No order found with this name");
        return o;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<OrderDTO> ordersA(List<OrderState> orderState, Integer limit, String searchText) throws OrderNotFoundException {
        List<OrderDTO> orders = orderService.getOrders(null, null, null);
        return orders;
    }


}
