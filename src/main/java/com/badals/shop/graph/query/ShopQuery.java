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
public class ShopQuery extends BaseQuery implements GraphQLQueryResolver {

   private static final Logger log = LoggerFactory.getLogger(ShopQuery.class);
   private final TenantProductService productService;
   private final HashtagService hashtagService;
   private final CategoryService categoryService;
   private final UserService userService;
   private final TenantService tenantService;
   private final TenantAdminProductService tenantAdminProductService;
   private final TenantCartService cartService;
   private final TenantOrderService orderService;
   private final TenantAccountService accountService;
   private final TenantLayoutService layoutService;
   public ShopQuery(TenantProductService productService, HashtagService hashtagService, CategoryService categoryService, UserService userService, TenantService tenantService, TenantSetupService tenantSetupService, TenantAdminProductService tenantAdminProductService, TenantCartService cartService, TenantOrderService orderService, TenantAccountService accountService, TenantLayoutService publicService) {
      this.productService = productService;
      this.hashtagService = hashtagService;
      this.categoryService = categoryService;
      this.userService = userService;
      this.tenantService = tenantService;
      this.tenantAdminProductService = tenantAdminProductService;
      this.cartService = cartService;
      this.orderService = orderService;
      this.accountService = accountService;
      this.layoutService = publicService;
   }
   public TenantDTO currentTenant() {
      return tenantService.findAll().get(0);
   }
   public ProductResponse adminSearchTenantProducts(String upc, String title) {
      return tenantAdminProductService.adminSearchTenantProducts(upc, title);
   }
   public ProductResponse tenantTagProducts(String hashtag) {
      return productService.findByHashtag(hashtag);
   }
   public ProductDTO tenantProduct(String slug) throws ProductNotFoundException {
      return productService.findProductBySlug(slug);
   }
   public OrderResponse tenantOrders(List<OrderState> orderState, Integer limit, Integer offset) {
      return accountService.orders(orderState, limit, offset);
   }
   public OrderDTO tenantOrder(String ref) throws OrderNotFoundException {
      return accountService.findOrderByRef(ref);
   }
   public CartDTO cart(String secureKey) {
      return cartService.updateCart(secureKey, null, false);
   }
   public List<ProfileHashtagDTO> tenantTags() {
      return productService.tenantTags();
   }
   //@PreAuthorize("hasRole('ROLE_ADMIN')")
   public OrderDTO orderSummary(String ref, String confirmationKey) throws OrderNotFoundException {
      OrderDTO o = orderService.getOrderConfirmation(ref, confirmationKey);
      if (o == null) throw new OrderNotFoundException("No order found with this name");
      return o;
   }
   public List<Attribute> getSliders() {
      return layoutService.getSliders();
   }
   public TenantDTO tenantInfo() {
      return layoutService.getTenant();
   }
   public List<Attribute> tenantSliders() {
      return layoutService.getSliders();
   }
   public List<Attribute> socialProfiles() {
      return layoutService.getSocial();
   }
}

