package com.badals.shop.service.query;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.pojo.MerchantProductResponse;
import com.badals.shop.service.CategoryService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.pojo.PartnerProductDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class PartnerQuery extends ShopQuery implements GraphQLQueryResolver {

   private final ProductService productService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(PartnerQuery.class);

   private final UserService userService;

   public PartnerQuery(ProductService productService, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PartnerProductDTO partnerProduct(Long id) {
       return null;
    }


}

