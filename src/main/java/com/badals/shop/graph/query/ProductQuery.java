package com.badals.shop.graph.query;

import com.badals.shop.domain.Customer;
import com.badals.shop.graph.HashtagResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.CategoryDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;

import com.badals.shop.xtra.amazon.IncorrectDimensionsException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.ebay.EbayLookup;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ProductQuery extends ShopQuery implements GraphQLQueryResolver {

   private final ProductService productService;
   private final ProductIndexService productIndexService;
   private final HashtagService hashtagService;
   //private final MwsLookup mwsLookup;
   private final EbayLookup ebayLookup;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(ProductQuery.class);

   private final UserService userService;


   public ProductQuery(ProductService productService, ProductIndexService productIndexService, HashtagService hashtagService, EbayLookup ebayLookup, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.productIndexService = productIndexService;
      this.hashtagService = hashtagService;
      this.ebayLookup = ebayLookup;
      this.categoryService = categoryService;
      this.userService = userService;
   }

   public List<ProductDTO> products(final int count) {
      return productService.getAllProducts(count);
      //return null;
   }
   public ProductDTO product (String slug, String cookie, String _locale) throws ProductNotFoundException, NoOfferException, PricingException, IncorrectDimensionsException, ExecutionException, InterruptedException {


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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductResponse findByKeyword(String keyword) {
        return productIndexService.findByKeyword(keyword);
    }


    public ProductResponse findByKeywordIndex(String keyword) throws NoOfferException {
        return productIndexService.findFromPas(keyword);
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
            return productIndexService.searchAll(type);
         case "capitol-stores":
            return productIndexService.searchAll(type);
      }
       return productIndexService.findByType(type);//.findAllByCategory(slug, offset, limit);
    }

   public List<ProductDTO> relatedProducts(String type, String slug) {
      return productService.findRelated(slug);
   }

   public HashtagResponse relatedTo(Long ref, List<String> hashtags, String title) {
      return hashtagService.findRelatedTo(ref, hashtags, title);
   }

   public List<CategoryDTO> categories(String type) {
      return categoryService.findAll();
   }

   public CategoryDTO category(int id) {
      return categoryService.findOne((long) id).orElse(null);
   }

   public ProductDTO getProductBySku(final String sku, final boolean isParent, final String _locale) throws ProductNotFoundException, PricingException, NoOfferException, IncorrectDimensionsException {
      log.info("GetProductBySky: pasService.lookup("+sku+")");
      ProductDTO product;
/*      if(isParent)
         return  productService.lookupPas(sku, true,true, false);
      return productService.lookupPas(sku, true, false);*/
      return null;
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

/*   public ProductDTO pas(String sku) throws ProductNotFoundException, NoOfferException, PricingException, IncorrectDimensionsException {
      return productService.lookupForcePas(sku, false,false, true);
   }*/

/*
   public ProductDTO pasUk(String sku) throws ProductNotFoundException, NoOfferException, PricingException {
      return productService.lookupForcePasUk(sku, false,false, true);
   }
*/

   public HashtagResponse hashtags(String tenant, Integer offset,Integer limit ) {
      return hashtagService.findForList(tenant, offset, limit);
   }

   public HashtagResponse hashtagsWithProducts(Integer offset,Integer limit ) {
      return hashtagService.findForListWithProducts(offset, limit);
   }
   public ProductResponse hashtagProducts(String tenant, String tag ) {
      return hashtagService.hashtagProducts(tenant, tag);
   }

}