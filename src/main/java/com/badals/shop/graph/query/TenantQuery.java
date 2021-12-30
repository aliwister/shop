package com.badals.shop.graph.query;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.enumeration.Currency;
import com.badals.shop.domain.pojo.I18String;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.graph.PartnerProductResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.*;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.TenantCartService;
import com.badals.shop.service.TenantProductService;
import com.badals.shop.service.TenantService;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static com.badals.shop.domain.enumeration.Currency.*;

@Component
public class TenantQuery extends ShopQuery implements GraphQLQueryResolver {

   private final TenantProductService partnerService;
   private final HashtagService hashtagService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(TenantQuery.class);

   private final UserService userService;
   private final TenantService tenantService;
   private final TenantCartService cartService;

   public TenantQuery(TenantProductService productService, HashtagService hashtagService, CategoryService categoryService, UserService userService, TenantService tenantService, TenantCartService cartService) {
      this.partnerService = productService;
      this.hashtagService = hashtagService;
      this.categoryService = categoryService;
      this.userService = userService;
      this.tenantService = tenantService;
      this.cartService = cartService;
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PartnerProduct partnerProduct(Long id, String _locale) {
       return partnerService.getPartnerProduct(id);
    }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public PartnerProductResponse partnerProducts(String search, Integer limit, Integer offset, Boolean active, String _locale) {
      return partnerService.findPartnerProducts(search, limit, offset, active);
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public VariationOption variationOptions(String name, String _locale) {
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
   public List<VariationOption> variations(String _locale) {
      return new ArrayList<VariationOption>(){{
         add(new VariationOption("size", "Size", new ArrayList<String>(){{add("Small"); add("Medium"); add("Large");}}));
         add(new VariationOption("color", "Color", new ArrayList<String>(){{add("Blue"); add("Red"); add("Yellow");}}));
         add(new VariationOption("packsize", "Pack Size", new ArrayList<String>(){{add("Single"); add("Bag"); add("Big Box");}}));
      }};
   }
   public List<HashtagDTO> hashtagList(String _locale) {
      return hashtagService.findAll();
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public List<I18String> brands(String _locale) {
      return new ArrayList<I18String>(){{
         add(new I18String("en", "Coach"));
         add(new I18String("en", "DKNY"));
         add(new I18String("en", "Michael Kors"));
         add(new I18String("en", "Guess"));
      }};
   }
   @PreAuthorize("hasRole('ROLE_MERCHANT')")
   public List<I18String> collections(String _locale) {
      return new ArrayList<I18String>(){{
         add(new I18String("en", "Fashion"));
         add(new I18String("en", "Auto Performance"));
         add(new I18String("en", "Power Food"));
         add(new I18String("en", "Mommy'n Me"));
      }};
   }

   public List<Currency> currencies(String _locale) {
      return new ArrayList<com.badals.shop.domain.enumeration.Currency>(){{
         add(OMR);
         add(AED);
         add(SAR);
         add(KWD);
         add(QAR);
      }};
   }

   public TenantDTO tenantByName(String name, String _locale) {
      return tenantService.findOneByName(name);
   }


   public List<ProfileHashtagDTO> tenantTags (Long tenantId, String _locale) {
      if (tenantId == null) {
         tenantId = TenantContext.getCurrentProfileId();
      }
      return partnerService.getTags(tenantId);
   }
   public ProductResponse tenantTagProducts(String hashtag, Long tenantId, String _locale) {
      if (tenantId == null) {
         tenantId = TenantContext.getCurrentProfileId();
      }
      return partnerService.findByHashtagAndTenantId(hashtag, tenantId);
   }

   public ProductDTO tenantProduct(String slug, String _locale) throws ProductNotFoundException {
      return partnerService.findProductBySlug(slug);
   }

   //partnerOrders(state: [OrderState], offset: Int, limit: Int, searchText: String): OrderResponse
   //partnerOrder(id: ID): Order

   public CartDTO cart(String secureKey, String _locale) {
      return cartService.updateCart(secureKey,null,false);
   }
}

