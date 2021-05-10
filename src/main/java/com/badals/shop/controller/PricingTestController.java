package com.badals.shop.controller;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@Controller
@RequestMapping("/pricing")
public class PricingTestController {

   private final PasLookup pasLookup;


   public PricingTestController(@Qualifier("us") PasLookup pasLookup) {
      this.pasLookup = pasLookup;
   }
   @RequestMapping(path="/pas/{sku}")
   @ResponseBody public GetItemsResponse pas(@PathVariable("sku") String sku) {
      return pasLookup.lookup(Arrays.asList(sku));
   }

   @RequestMapping(path="/pasvar/{sku}/{page}")
   @ResponseBody public GetVariationsResponse pasVar(@PathVariable("sku") String sku, @PathVariable("page") Integer page) {
      return pasLookup.variationLookup(sku, page);
   }
}
