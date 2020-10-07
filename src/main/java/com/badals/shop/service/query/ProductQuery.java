package com.badals.shop.service.query;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Hashtag;
import com.badals.shop.domain.pojo.HashtagResponse;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.service.CategoryService;
import com.badals.shop.service.HashtagService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CategoryDTO;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;

import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.ebay.EbayLookup;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductQuery extends ShopQuery implements GraphQLQueryResolver {

   private final ProductService productService;
   private final HashtagService hashtagService;
   //private final MwsLookup mwsLookup;
   private final EbayLookup ebayLookup;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(ProductQuery.class);

   private final UserService userService;

   public ProductQuery(ProductService productService, HashtagService hashtagService, EbayLookup ebayLookup, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.hashtagService = hashtagService;
      this.ebayLookup = ebayLookup;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   public List<ProductDTO> products(final int count) {
      return productService.getAllProducts(count);
      //return null;
   }
   public ProductDTO product (String slug, String cookie) throws ProductNotFoundException, NoOfferException, PricingException {
      ProductDTO dto = this.productService.getProductBySlug(slug);
      Customer loginUser = userService.getUserWithAuthorities().orElse(null);
      //CompletableFuture.supplyAsync(() -> productService.log(loginUser, slug, cookie))


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
         case "capitol-stores":
            return productService.searchAll(type);
      }
       return productService.findByType(type);//.findAllByCategory(slug, offset, limit);
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

   public ProductDTO getProductByDial(final String dial) throws ProductNotFoundException, PricingException, NoOfferException {
      log.info("getProductByDial: ("+dial+")");
      return productService.getProductByDial(dial);
   }

   public List<ProductDTO> pendingMerchantProducts(Long merchantId) {
      return null;
   }

   public ProductDTO mws(String asin) {
      //mwsLookup.lookup(asin);
      return null;
   }

   public ProductDTO ebay(String id) throws ProductNotFoundException, NoOfferException, PricingException {
      return productService.lookupEbay(id);
   }

   public ProductDTO pas(String sku) throws ProductNotFoundException, NoOfferException, PricingException {
      return productService.lookupForcePas(sku, false,false, true);
   }

   public HashtagResponse hashtags(Integer offset,Integer limit ) {
      return hashtagService.findForList(offset, limit);
   }

   public HashtagResponse hashtagsWithProducts(Integer offset,Integer limit ) {
      return hashtagService.findForListWithProducts(offset, limit);
   }
}