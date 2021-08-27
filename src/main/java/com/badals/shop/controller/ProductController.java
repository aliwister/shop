package com.badals.shop.controller;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.amazon.paapi5.v1.SearchItemsResponse;
import com.amazonservices.mws.products.model.Product;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.ItemNotAccessibleException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.keepa.KeepaResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;

@Controller
@RequestMapping("/api")
public class ProductController {

   private final Logger log = LoggerFactory.getLogger(ProductController.class);
   private final ProductService productService;

   public ProductController(ProductService productService) {
      this.productService = productService;
   }

   @RequestMapping(path="/product/{slug}")
   @ResponseBody
   @ResponseStatus(OK)
   public Mono<ProductDTO> slug(@PathVariable("slug") String sku) throws PricingException, NoOfferException, ExecutionException, InterruptedException, ProductNotFoundException {
      Mono<ProductDTO> mono =  productService.getBySlugMono(sku);
      return mono;
   }

   @RequestMapping(path="/product/sku",method = RequestMethod.POST)
   @ResponseBody
   @ResponseStatus(OK)
   public Mono<ProductDTO> sku(@RequestParam("sku") String sku) throws PricingException, NoOfferException, ExecutionException, InterruptedException, ProductNotFoundException {
      return productService.lookupMono(sku, true);
   }
}
