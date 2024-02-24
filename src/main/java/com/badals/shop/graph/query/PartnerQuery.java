package com.badals.shop.graph.query;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.enumeration.Currency;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.I18String;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.domain.tenant.Page;
import com.badals.shop.domain.tenant.TenantCartRule;
import com.badals.shop.domain.tenant.TenantFaqCategory;
import com.badals.shop.domain.tenant.TenantFaqQA;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import com.badals.shop.service.dto.TenantCartRuleDTO;
import com.badals.shop.service.pojo.Partner;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.pojo.PartnerStock;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.badals.shop.domain.enumeration.Currency.*;
@Component
public class PartnerQuery extends BaseQuery implements GraphQLQueryResolver {

   private final TenantAdminProductService productService;
   private final TenantAdminOrderService orderService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(PartnerQuery.class);

   private final UserService userService;
   private final TenantSetupService setupService;
   private final PageInfoService pageInfoService;
   private final FaqCategoryService faqCategoryService;
   private final FaqQAService faqQAService;
   private final TenantCartRuleService tenantCartRuleService;
   private final TenantStockService tenantStockService;

   public PartnerQuery(TenantAdminProductService productService, TenantAdminOrderService orderService, CategoryService categoryService, UserService userService, TenantSetupService setupService, PageInfoService pageInfoService, FaqCategoryService faqCategoryService, FaqQAService faqQAService, TenantCartRuleService tenantCartRuleService, TenantStockService tenantStockService) {
      this.productService = productService;
      this.orderService = orderService;
      this.categoryService = categoryService;
      this.userService = userService;
      this.setupService = setupService;
      this.pageInfoService = pageInfoService;
      this.faqCategoryService = faqCategoryService;
      this.faqQAService = faqQAService;
      this.tenantCartRuleService = tenantCartRuleService;
      this.tenantStockService = tenantStockService;
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PartnerProduct partnerProduct(String id) {
       return productService.getPartnerProduct(id);
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public ProductResponse partnerProducts(String search, Integer limit, Integer offset, Boolean active) {
      return productService.findPartnerProducts(search, limit, offset, active);
   }
   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public VariationOption variationOptions(String name) {
      if (name.equals("size")) {
         return new VariationOption("size", "Size", new ArrayList<String>(){{add("Small"); add("Medium"); add("Large");}});
      }
      else if (name.equals("color")) {
         return new VariationOption("color", "Color", new ArrayList<String>(){{add("Blue"); add("Red"); add("Yellow");}});
      }
      else if (name.equals("packsize")) {
         return new VariationOption("packsize", "Pack Size", new ArrayList<String>(){{add("Single"); add("Bag"); add("Big Box");}});
      }
      return null;
   }
   public List<VariationOption> variations() {
      return new ArrayList<VariationOption>(){{
         add(new VariationOption("size", "Size", new ArrayList<String>(){{add("Small"); add("Medium"); add("Large");}}));
         add(new VariationOption("color", "Color", new ArrayList<String>(){{add("Blue"); add("Red"); add("Yellow");}}));
         add(new VariationOption("packsize", "Pack Size", new ArrayList<String>(){{add("Single"); add("Bag"); add("Big Box");}}));
      }};
   }

   public List<ProfileHashtagDTO> partnerTenantTags () {
      return setupService.tenantTags();
   }

   public List<Attribute> deliveryProfiles () {
      return List.of(new Attribute("deliveryProfile", "default"), new Attribute("deliveryProfile", "nol+dhl"), new Attribute("deliveryProfile", "pickup"));
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public List<I18String> brands() {
      return new ArrayList<I18String>(){{
         add(new I18String("en", "Coach"));
         add(new I18String("en", "DKNY"));
         add(new I18String("en", "Michael Kors"));
         add(new I18String("en", "Guess"));
      }};
   }
   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public List<I18String> collections() {
      return new ArrayList<I18String>(){{
         add(new I18String("en", "Fashion"));
         add(new I18String("en", "Auto Performance"));
         add(new I18String("en", "Power Food"));
         add(new I18String("en", "Mommy'n Me"));
      }};
   }

   public List<Currency> currencies() {
      return new ArrayList<com.badals.shop.domain.enumeration.Currency>(){{
         add(OMR);
         add(AED);
         add(SAR);
         add(KWD);
         add(QAR);
         add(USD);
      }};
   }
/*
   public TenantDTO tenantByName(String name) {
      return tenantService.findOneByName(name);
   }*/


    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public ProductResponse getStock(Integer limit, Integer offset){
        return tenantStockService.getStock(limit, offset);
    }

   //partnerOrders(state: [OrderState], offset: Int, limit: Int, searchText: String): OrderResponse
   //partnerOrder(id: ID): Order
   //@PreAuthorize("hasRole('ROLE_MERCHANT')")
   public OrderResponse partnerOrders(List<OrderState> orderState, Integer offset, Integer limit, String searchText, Boolean balance) throws OrderNotFoundException {
      return orderService.getOrders(orderState, offset, limit, searchText, balance);
      //return orders;
   }

   //@PreAuthorize("hasRole('ROLE_ADMIN')")
   public OrderDTO partnerOrder(Long id) throws OrderNotFoundException {
      OrderDTO o = orderService.getOrderWithOrderItems(id).orElse(null);
      if(o == null) throw new OrderNotFoundException("No order found with this name");
      return o;
   }
   public Partner currentPartner(String locale) {
      return setupService.findAllPartners().get(0);
   }

   public List<Attribute> social(String locale) {
      return setupService.getSocial(locale);
   }

   public List<Attribute> sliders(String locale) {
      return setupService.getSliders(locale);
   }
    @PreAuthorize("hasRole('ROLE_USER')")
    public Page pageInfos(String slug) {
       return pageInfoService.getPageInfosBySlug(slug);
   }

   @PreAuthorize("hasRole('ROLE_USER')")
   public List<Page> pagesInfos(){
       return pageInfoService.getPages();
   }

   @PreAuthorize("hasRole('ROLE_USER')")
   public List<TenantFaqCategory> faqCategories() {
      return faqCategoryService.getFaqCategories();
   }

   @PreAuthorize("hasRole('ROLE_USER')")
   public TenantFaqCategory faqCategoryById(Long id) {
      return faqCategoryService.getFaqCategoryById(id);
   }

   @PreAuthorize("hasRole('ROLE_USER')")
   public List<TenantFaqQA> faqQAs(){
       return faqQAService.getFaqQAs();
   }

    @PreAuthorize("hasRole('ROLE_USER')")
    public TenantFaqQA faqQAById(Long id) {
        return faqQAService.getFaqQAById(id);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public List<TenantCartRuleDTO> coupons() {
        return tenantCartRuleService.getCartRules();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public TenantCartRuleDTO couponByID(String coupon) {
        return tenantCartRuleService.getCartRuleByCoupon(coupon);
    }
}

