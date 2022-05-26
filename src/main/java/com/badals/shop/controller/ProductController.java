package com.badals.shop.controller;


import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.concurrent.ExecutionException;

import static org.springframework.http.HttpStatus.OK;

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
   public Mono<ProductDTO> slug(@PathVariable("slug") String sku) throws ExecutionException, InterruptedException, ProductNotFoundException {
      Mono<ProductDTO> mono =  productService.getBySlugMono(sku);
      return mono;
   }

/*   @RequestMapping(path="/product/sku",method = RequestMethod.POST)
   @ResponseBody
   @ResponseStatus(OK)
   public Mono<ProductDTO> sku(@RequestParam("sku") String sku) throws ExecutionException, InterruptedException, ProductNotFoundException {
      return productService.lookupMono(sku, true);
   }*/
}
