package com.badals.shop.graph.query;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.*;
import com.badals.shop.web.rest.errors.ProductNotFoundException;

import com.coxautodev.graphql.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.concurrent.ExecutionException;

@Component
public class ProductSubscription  implements GraphQLSubscriptionResolver {

   private final ProductService productService;
   private final ProductIndexService productIndexService;
   private final HashtagService hashtagService;
   //private final MwsLookup mwsLookup;

   private final CategoryService categoryService;
   private static final Logger log = LoggerFactory.getLogger(ProductSubscription.class);

   private final UserService userService;


   public ProductSubscription(ProductService productService, ProductIndexService productIndexService, HashtagService hashtagService, CategoryService categoryService, UserService userService) {
      this.productService = productService;
      this.productIndexService = productIndexService;
      this.hashtagService = hashtagService;
      this.categoryService = categoryService;
      this.userService = userService;
   }


   public Publisher<Attribute> getProductBySku(final String sku, final boolean isParent, String _locale) throws ProductNotFoundException, ExecutionException, InterruptedException {
      log.info("GetProductBySky: pasService.lookup("+sku+")");
      //ProductDTO product;
      Flux<Attribute> just = Flux.just(new Attribute("a","1"), new Attribute("b","2"));
         //return  productService.lookupPas(sku, true,true, false);
      //ProductDTO dto = productService.lookupPas(sku, true, false);
      return just;
   }

}