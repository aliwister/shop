package com.badals.shop.service.query;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.pojo.MerchantProductResponse;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.service.CategoryService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CategoryDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MerchantQuery extends ShopQuery implements GraphQLQueryResolver {

   private final ProductService productService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(MerchantQuery.class);

   private final UserService userService;

   public MerchantQuery(ProductService productService, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public MerchantProductResponse merchantProducts(String text, String type, Integer offset, Integer limit, String lang) throws IllegalAccessException {
       String t =  TenantContext.getCurrentTenant();
       log.info("Tenant: " + t);
       return productService.getForMerchant(TenantContext.getCurrentTenantId(),limit);
    }

}

