package com.badals.shop.service.query;

import com.algolia.search.SearchIndex;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.service.CategoryService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CategoryDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.web.rest.errors.ProductNotFoundException;

import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductQuery extends ShopQuery implements GraphQLQueryResolver {

   private final ProductService productService;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(ProductQuery.class);

   private final UserService userService;

   public ProductQuery(ProductService productService, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   public List<ProductDTO> products(final int count) {
      return productService.getAllProducts(count);
      //return null;
   }
   public ProductDTO product (String slug)  throws ProductNotFoundException  {
      ProductDTO dto = this.productService.getProductBySlug(slug);
      return dto;
   }

   @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO getProductAdmin(final int id) {
        //return new Product();
        return productService.getProductAdmin(id).get();
    }

    public ProductDTO getProductAny(final int id) {
        //return new Product();
        return productService.getProduct(id).get();
    }

    public ProductResponse products(String slug, String text, String type, Integer offset, Integer limit, String lang) {
      switch(type){
         case "LATEST":
            return productService.getLatest(10);
         case "MAYASEEN":
            return productService.searchAll(type);
      }
       return productService.findAllByCategory(slug, offset, limit);
    }

   public List<ProductDTO> relatedProducts(String type, String slug) {
      return productService.findRelated(slug);
   }

   public List<CategoryDTO> categories(String type) {
      return categoryService.findAll();
   }

   public CategoryDTO category(int id) {
      return categoryService.findOne((long) id).orElse(null);
   }

   public ProductDTO getProductBySku(final String sku, final boolean isParent) throws ProductNotFoundException, PricingException, NoOfferException {
      log.info("GetProductBySky: pasService.lookup("+sku+")");
      ProductDTO product;
      if(isParent)
         return  productService.lookupPas(sku, true,true, false);
      return productService.lookupPas(sku, true, false);
   }

   public List<ProductDTO> pendingMerchantProducts(Long merchantId) {
      return null;
   }
}

