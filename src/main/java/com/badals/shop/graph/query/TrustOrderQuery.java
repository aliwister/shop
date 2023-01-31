package com.badals.shop.graph.query;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.service.ActionService;
import com.badals.shop.service.TenantOrderService;
import com.badals.shop.service.TenantPaymentService;
import com.badals.shop.service.dto.ActionDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TrustOrderQuery extends BaseQuery implements GraphQLQueryResolver {

   private final TenantOrderService orderService;
   private final TenantPaymentService paymentService;
   private final ActionService actionService;


   public TrustOrderQuery(TenantOrderService orderService, TenantPaymentService paymentService, ActionService actionService) {
      this.orderService = orderService;
      this.paymentService = paymentService;
      this.actionService = actionService;
   }


   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public OrderDTO orderA(Long id) throws OrderNotFoundException {
      OrderDTO o = orderService.getOrderWithOrderItems(id).orElse(null);
      if(o == null) throw new OrderNotFoundException("No order found with this name");
      return o;
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public OrderResponse ordersA(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance, Boolean isAsc, BigDecimal minBal) throws OrderNotFoundException {
      return orderService.getOrders(orderState, offset, limit, searchText, balance, isAsc, minBal);
      //return orders;
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public List<PaymentDTO> payments(long orderId) {
      return paymentService.findForOrder(orderId);
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public List<ActionDTO> auditActivity(Long id, String type) {
      return actionService.auditActivity(id, type);
   }
}
