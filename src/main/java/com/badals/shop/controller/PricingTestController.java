package com.badals.shop.controller;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RequestMapping("/pricing")
public class ApiController {

   private final PasLookup pasLookup;


   public ApiController(PasLookup pasLookup) {
      this.pasLookup = pasLookup;
   }
   @GetMapping("/pas")
   @ResponseBody public GetItemsResponse pas(@PathVariable String sku) {
      return pasLookup.lookup(Arrays.asList(sku));
   }
}
