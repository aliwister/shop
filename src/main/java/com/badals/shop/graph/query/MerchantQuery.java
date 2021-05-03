package com.badals.shop.graph.query;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.graph.MerchantProductResponse;
import com.badals.shop.service.CategoryService;
import com.badals.shop.service.ProductIndexService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class MerchantQuery extends ShopQuery implements GraphQLQueryResolver {

   private final ProductService productService;
   private final ProductIndexService productIndexService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(MerchantQuery.class);

   private final UserService userService;

   public MerchantQuery(ProductService productService, ProductIndexService productIndexService, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.productIndexService = productIndexService;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public MerchantProductResponse merchantProducts(String text, String type, Integer offset, Integer limit, String lang, Boolean imported) throws IllegalAccessException {
       String t =  TenantContext.getCurrentTenant();
       log.info("Tenant: "+ t+ " TenantId: "+ TenantContext.getCurrentTenantId()+ " Merchant "+ TenantContext.getCurrentMerchant()+ " MerchantId "+ TenantContext.getCurrentMerchantId());
       //return productService.getForTenant(TenantContext.getCurrentTenantId(),limit, offset);
       return productIndexService.searchForTenant(t, text, limit, offset, imported);
    }

/*    public MerchantProductResponse merchantProductsCatalog(String text, String type, Integer offset, Integer limit, String lang) throws IllegalAccessException {
       String t =  TenantContext.getCurrentTenant();
       log.info("Tenant: "+ t+ " TenantId: "+ TenantContext.getCurrentTenantId()+ " Merchant "+ TenantContext.getCurrentMerchant()+ " MerchantId "+ TenantContext.getCurrentMerchantId());
       return productService.searchForTenant(t, text, limit, offset);
    }*/
}

