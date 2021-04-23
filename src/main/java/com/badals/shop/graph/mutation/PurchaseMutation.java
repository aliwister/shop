package com.badals.shop.graph.mutation;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PaymentService;
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
public class PurchaseMutation implements GraphQLMutationResolver {

    final private OrderService orderService;
    final private PurchaseService purchaseService;
    final private PaymentService paymentService;

    public PurchaseMutation(OrderService orderService, PurchaseService purchaseService, PaymentService paymentService) {
        this.orderService = orderService;
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO createPurchase(PurchaseDTO dto) {
        PurchaseDTO purchase = purchaseService.save(dto);
        return purchase;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO updatePurchase(PurchaseDTO dto, List<PurchaseItemDTO> items) {
        PurchaseDTO purchase = purchaseService.updatePurchase(dto, items);
        return purchase;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO cancelPurchase(Long id, String reason){
        PurchaseDTO order = purchaseService.cancel(id, reason);
        return order;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO closePurchase(Long id, String reason){
        PurchaseDTO order = purchaseService.close(id, reason);
        return order;
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO setPurchaseState(Long id, OrderState state){
        return purchaseService.setStatus(id, state);
    }


}

