package com.badals.shop.service.query;

import com.badals.shop.service.CategoryService;
import com.badals.shop.service.PartnerService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.pojo.PartnerProduct;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class PartnerQuery extends ShopQuery implements GraphQLQueryResolver {

   private final PartnerService partnerService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(PartnerQuery.class);

   private final UserService userService;

   public PartnerQuery(PartnerService partnerService, CategoryService categoryService, UserService userService) {
      this.partnerService = partnerService;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PartnerProduct partnerProduct(Long id) {
       Long mId = 1L;//TenantContext.getCurrentMerchantId();
       return partnerService.getPartnerProduct(id, mId);
    }


}

