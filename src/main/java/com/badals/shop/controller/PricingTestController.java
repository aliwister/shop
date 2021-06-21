package com.badals.shop.controller;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.amazon.paapi5.v1.SearchItemsResponse;
import com.amazonservices.mws.products.model.Product;
import com.badals.shop.service.CartService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.ItemNotAccessibleException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.keepa.KeepaLookup;
import com.badals.shop.xtra.keepa.KeepaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Controller
@RequestMapping("/pricing")
public class PricingTestController {

   private final Logger log = LoggerFactory.getLogger(CartService.class);

   private final PasLookup pasLookup;
   private final MwsLookup mwsLookup;
   private final KeepaLookup keepaLookup;
   private final WebClient webClient;
   private final ProductService productService;

   public PricingTestController(@Qualifier("us") PasLookup pasLookup, MwsLookup mwsLookup, KeepaLookup keepaLookup, ProductService productService) {
      this.pasLookup = pasLookup;
      this.mwsLookup = mwsLookup;
      this.keepaLookup = keepaLookup;
      this.productService = productService;
      webClient = WebClient.create();
   }
   @RequestMapping(path="/pas/{sku}")
   @ResponseBody public GetItemsResponse pas(@PathVariable("sku") String sku) {
      return pasLookup.lookup(Arrays.asList(sku));
   }

   @RequestMapping(path="/pasvar/{sku}/{page}")
   @ResponseBody public GetVariationsResponse pasVar(@PathVariable("sku") String sku, @PathVariable("page") Integer page) {
      return pasLookup.variationLookup(sku, page);
   }

   @RequestMapping(path="/passearch/{keyword}")
   @ResponseBody public SearchItemsResponse pasVar(@PathVariable("keyword") String keyword) {
      return pasLookup.searchItems(keyword);
   }
   @RequestMapping(path="/mws/{sku}")
   @ResponseBody public Product mwsMatchingProduct(@PathVariable("sku") String sku) {
      return mwsLookup.getMatchingProduct(sku);
   }

   @RequestMapping(path="/mwsp/{sku}")
   @ResponseBody public MwsItemNode mwsOffers(@PathVariable("sku") String sku) {
      return mwsLookup.fetch(sku);
   }

   @RequestMapping(path="/keepam/{sku}")
   @ResponseBody public PasItemNode keepam(@PathVariable("sku") String sku) throws PricingException, ProductNotFoundException, ItemNotAccessibleException {
      return keepaLookup.lookup(sku, true);
   }

   @RequestMapping(path="/mono/{sku}")
   @ResponseBody
   @ResponseStatus(OK)
   public Mono<KeepaResponse> controllerMethod(@PathVariable("sku") String sku) {

      final UriComponentsBuilder builder =
              UriComponentsBuilder.fromHttpUrl("https://api.keepa.com/product?key=2fk5l4evnaer7h24jrn18ahu1vpk5o1dv692mdnmdqat1uuh8j9kn5l44mjismia&domain=1&rating=0&history=1&days=1")
                      .queryParam("asin", sku);
      return webClient
              .get()
              .uri(builder.build().encode().toUri())
              .accept(APPLICATION_JSON_UTF8)
              .retrieve()
              .bodyToMono(KeepaResponse.class)
              .retry(4)
              .doOnError(e -> log.error("Boom!", e))
              .map(s -> {

                 // This is your transformation step.
                 // Map is synchronous so will run in the thread that processed the response.
                 // Alternatively use flatMap (asynchronous) if the step will be long running.
                 // For example, if it needs to make a call out to the database to do the transformation.

                 return s;
              });
   }

   @RequestMapping(path="/product/{sku}")
   @ResponseBody
   @ResponseStatus(OK)
   public Mono<ProductDTO> product(@PathVariable("sku") String sku) throws PricingException, NoOfferException, ExecutionException, InterruptedException {
      return productService.lookupMono(sku);
   }
}
