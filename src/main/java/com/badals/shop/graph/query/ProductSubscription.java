package com.badals.shop.graph.query;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.pojo.Attribute;
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
import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Component
public class ProductSubscription  implements GraphQLSubscriptionResolver {

   private final ProductService productService;
   private final ProductIndexService productIndexService;
   private final HashtagService hashtagService;
   //private final MwsLookup mwsLookup;
   private final EbayLookup ebayLookup;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(ProductSubscription.class);

   private final UserService userService;


   public ProductSubscription(ProductService productService, ProductIndexService productIndexService, HashtagService hashtagService, EbayLookup ebayLookup, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.productIndexService = productIndexService;
      this.hashtagService = hashtagService;
      this.ebayLookup = ebayLookup;
      this.categoryService = categoryService;
      this.userService = userService;
   }


   public Publisher<Attribute> getProductBySku(final String sku, final boolean isParent, String _locale) throws ProductNotFoundException, PricingException, NoOfferException, IncorrectDimensionsException, ExecutionException, InterruptedException {
      log.info("GetProductBySky: pasService.lookup("+sku+")");
      //ProductDTO product;
      Flux<Attribute> just = Flux.just(new Attribute("a","1"), new Attribute("b","2"));
         //return  productService.lookupPas(sku, true,true, false);
      //ProductDTO dto = productService.lookupPas(sku, true, false);
      return just;
   }

}