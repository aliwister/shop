package com.badals.shop.service.query;


import com.badals.shop.domain.Merchant;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.MerchantDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PurchaseQuery extends ShopQuery implements GraphQLQueryResolver {

    private final PurchaseService purchaseService;

    private final MerchantService merchantService;

    public PurchaseQuery(PurchaseService purchaseService, MerchantService merchantService) {
        this.purchaseService = purchaseService;
        this.merchantService = merchantService;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO purchase(Long id) throws OrderNotFoundException {
        PurchaseDTO o = purchaseService.findForPurchaseDetails(id).orElse(null);
        if(o == null) throw new OrderNotFoundException("No order found with this name");
        return o;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PurchaseDTO> purchases(List<OrderState> orderState, Integer limit, String searchText) throws OrderNotFoundException {
        List<PurchaseDTO> orders = purchaseService.findForPurchaseList(orderState, limit, searchText);
        return orders;
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PurchaseQueue> purchaseQueue() {
        return purchaseService.getPurchaseQueue();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<MerchantDTO> merchants() { return merchantService.findAll();}
}
