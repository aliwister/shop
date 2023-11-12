package com.badals.shop.graph.query;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Reward;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.tenant.*;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.*;
import com.badals.shop.service.TenantCartService;
import com.badals.shop.service.TenantProductService;
import com.badals.shop.service.TenantService;
import com.badals.shop.service.mapper.CustomerMapper;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ShopQuery extends BaseQuery implements GraphQLQueryResolver {

   private static final Logger log = LoggerFactory.getLogger(ShopQuery.class);
   private final TenantProductService productService;
   private final CategoryService categoryService;
   private final CustomerService customerService;
   private final CustomerMapper customerMapper;
   private final TenantService tenantService;
   private final TenantAdminProductService tenantAdminProductService;
   private final TenantCartService cartService;
   private final TenantWishListService wishlistService;
   private final TenantOrderService orderService;
   private final TenantAccountService accountService;
   private final TenantLayoutService layoutService;
    private final PageInfoPublicService pageInfoService;
    private final FaqPublicService faqService;
    private final TenantRewardService rewardService;
    public ShopQuery(TenantProductService productService, CategoryService categoryService, CustomerService customerService, TenantService tenantService, TenantSetupService tenantSetupService, CustomerMapper customerMapper, TenantAdminProductService tenantAdminProductService, TenantCartService cartService, TenantOrderService orderService, TenantAccountService accountService, TenantLayoutService publicService, TenantWishListService wishlistService, PageInfoPublicService pageInfoService, FaqPublicService faqService, TenantRewardService rewardService) {
      this.productService = productService;
      this.categoryService = categoryService;
      this.customerService = customerService;
      this.tenantService = tenantService;
      this.customerMapper = customerMapper;
      this.tenantAdminProductService = tenantAdminProductService;
      this.cartService = cartService;
      this.orderService = orderService;
      this.accountService = accountService;
      this.layoutService = publicService;
      this.wishlistService = wishlistService;
      this.pageInfoService = pageInfoService;
      this.faqService = faqService;
      this.rewardService = rewardService;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    public TenantWishList wishlist() {
       //todo check for not null and refactor to service
       //System.out.println(TenantContext.getCurrentProfileId());
       UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       Customer customer = customerService.findByEmail(userDetails.getUsername());
       Long customer_id = customer.getId();
//       System.out.println(userDetails.getUsername());
       TenantWishList tenantWishList = wishlistService.getCustomerWishListByCustomerAndTenant(TenantContext.getCurrentProfile(),customer_id);
//        System.out.println(tenantWishList);
        return tenantWishList;
       //       long h = 123;
//       return new WishListDTO(h);
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

   //@PreAuthorize("hasRole('ROLE_USER')")
   public CustomerDTO me() {
      return cartService.me();
   }

   //@PreAuthorize("hasRole('ROLE_USER')")
   public CustomerDTO mePlus() {
      Customer customer = customerService.getUserWithAuthorities().orElse(null);
      if(customer != null)
         return customerService.findOne(customer.getId()).orElse(null);
      return null;
   }

   public Checkout plusCart(final String secureKey) {
      return cartService.plusCart(secureKey);
   }

    public Page pageInfosShop(String slug) {
        return pageInfoService.getPageInfosBySlug(TenantContext.getCurrentProfile(), slug);
    }

    public List<Page> pagesInfosShop(){
        return pageInfoService.getPages(TenantContext.getCurrentProfile());
    }

    public List<TenantFaqCategory> faqCategoriesShop() {
        return faqService.getFaqCategories(TenantContext.getCurrentProfile());
    }

    public List<TenantFaqQA> faqQAsShop(){
        return faqService.getFaqQAs(TenantContext.getCurrentProfile());
    }

    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public List<TenantReward> getAffordableRewards(){
        return rewardService.getAffordableRewards();
    }

}

