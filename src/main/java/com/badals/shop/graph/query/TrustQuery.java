package com.badals.shop.graph.query;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.graph.PurchaseResponse;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.MerchantDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TrustQuery extends BaseQuery implements GraphQLQueryResolver {

   private final PurchaseService purchaseService;
   private final MerchantService merchantService;

   public TrustQuery(PurchaseService purchaseService, MerchantService merchantService) {
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
   public PurchaseResponse purchases(List<OrderState> orderState, Integer offset, Integer limit, String searchText) throws OrderNotFoundException {
      PurchaseResponse orders = purchaseService.findForPurchaseList(orderState, offset, limit, searchText);
      return orders;
   }
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public List<PurchaseQueue> unshippedPurchases() throws OrderNotFoundException {
      List<PurchaseQueue> orders = purchaseService.findUnshippedPurchases();
      return orders;
   }
   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public List<PurchaseQueue> purchaseQueue() {
      return purchaseService.getPurchaseQueue();
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
   public List<MerchantDTO> merchants() { return merchantService.findAll();}
}
