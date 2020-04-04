package com.badals.shop.service.mutation;

import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.OrderItemDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.service.dto.PurchaseItemDTO;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
    @Autowired
    private OrderService orderService;

    public OrderDTO createOrder(final Long id) {
        OrderDTO order = new OrderDTO();//this.orderService.createOrder(CartDTO cart);
        order.setId(id);
        return order;
    }

    public OrderDTO editOrder(Long id, List<OrderItemDTO> orderItems) {
        OrderDTO order = orderService.editOrderItems(id, orderItems);
        return order;
    }

}

