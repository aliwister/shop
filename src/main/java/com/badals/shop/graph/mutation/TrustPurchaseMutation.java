package com.badals.shop.graph.mutation;

import com.badals.shop.domain.Purchase;
import com.badals.shop.domain.enumeration.OrderState;

import com.badals.shop.service.AmazonPurchaseService;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.*;
import com.badals.shop.service.pojo.Message;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;


/*
mutation {
  createOrder(id: 5) {
    id
  }
}

 */

@Component
public class TrustPurchaseMutation implements GraphQLMutationResolver {

    final private PurchaseService purchaseService;
    final private AmazonPurchaseService amazonPurchaseService;

    public TrustPurchaseMutation(PurchaseService purchaseService, AmazonPurchaseService amazonPurchaseService) {
        this.purchaseService = purchaseService;
        this.amazonPurchaseService = amazonPurchaseService;
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



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendPurchaseToAmazon(Long id) throws IOException {
        PurchaseDTO p = purchaseService.findForPurchaseDetails(id).get();
        String ret = amazonPurchaseService.buy(p);

        return new Message(ret);
    }

}

