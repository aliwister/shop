package com.badals.shop.graph.query;

import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.*;
import com.badals.shop.service.TenantCartService;
import com.badals.shop.service.TenantProductService;
import com.badals.shop.service.TenantService;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TenantQuery extends ShopQuery implements GraphQLQueryResolver {

   private final TenantProductService productService;
   private final HashtagService hashtagService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(TenantQuery.class);

   private final UserService userService;
   private final TenantService tenantService;
   private final TenantSetupService tenantSetupService;
   private final TenantAdminProductService tenantAdminProductService;
   private final TenantCartService cartService;
   private final TenantOrderService orderService;
   private final TenantAccountService accountService;

   public TenantQuery(TenantProductService productService, HashtagService hashtagService, CategoryService categoryService, UserService userService, TenantService tenantService, TenantSetupService tenantSetupService, TenantAdminProductService tenantAdminProductService, TenantCartService cartService, TenantOrderService orderService, TenantAccountService accountService) {
      this.productService = productService;
      this.hashtagService = hashtagService;
      this.categoryService = categoryService;
      this.userService = userService;
      this.tenantService = tenantService;
      this.tenantSetupService = tenantSetupService;
      this.tenantAdminProductService = tenantAdminProductService;
      this.cartService = cartService;
      this.orderService = orderService;
      this.accountService = accountService;
   }

   public TenantDTO tenantByName(String name) {
      return tenantService.findOneByName(name);
   }
   public TenantDTO currentTenant() {
      //TenantContext.getCurrentProfile();
      return tenantSetupService.findAll().get(0);
   }

   public ProductResponse adminSearchTenantProducts(String upc, String title) {
      return tenantAdminProductService.adminSearchTenantProducts(upc, title);
   }

   public ProductResponse tenantTagProducts(String hashtag) {
      return productService.findByHashtag(hashtag);
   }

   public OrderResponse tenantOrders(List<OrderState> orderState, Integer limit, Integer offset) {
      return accountService.orders(orderState, limit, offset);
   }

   public OrderDTO tenantOrder(String ref) throws OrderNotFoundException {
      return accountService.findOrderByRef(ref);
   }

   public ProductDTO tenantProduct(String slug) throws ProductNotFoundException {
      return productService.findProductBySlug(slug);
   }

   //partnerOrders(state: [OrderState], offset: Int, limit: Int, searchText: String): OrderResponse
   //partnerOrder(id: ID): Order

   public CartDTO cart(String secureKey) {
      return cartService.updateCart(secureKey,null,false);
   }

   public List<ProfileHashtagDTO> tenantTags () {
      return productService.tenantTags();
   }

   //@PreAuthorize("hasRole('ROLE_ADMIN')")
   public OrderDTO orderSummary(String ref, String confirmationKey) throws OrderNotFoundException {
      OrderDTO o = orderService.getOrderConfirmation(ref, confirmationKey);
      if(o == null) throw new OrderNotFoundException("No order found with this name");
      return o;
   }

   public List<Attribute> getSliders(String locale) {
      return tenantSetupService.getSliders(locale);
   }
  public List<Attribute> tenantSliders(String locale) {
      return productService.getSliders(locale);
   }

}

