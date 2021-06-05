package com.badals.shop.controller;

import com.amazon.paapi5.v1.GetItemsResponse;
import com.amazon.paapi5.v1.GetVariationsResponse;
import com.amazon.paapi5.v1.SearchItemsResponse;
import com.amazonservices.mws.products.model.Product;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.ItemNotAccessibleException;
import com.badals.shop.xtra.amazon.PasItemMapper;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.keepa.KeepaLookup;
import com.badals.shop.xtra.keepa.KeepaMapper;
import com.badals.shop.xtra.keepa.KeepaResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequestMapping("/pricing")
public class PricingTestController {

   private final PasLookup pasLookup;
   private final MwsLookup mwsLookup;
   private final KeepaLookup keepaLookup;

   public PricingTestController(@Qualifier("us") PasLookup pasLookup, MwsLookup mwsLookup, KeepaLookup keepaLookup) {
      this.pasLookup = pasLookup;
      this.mwsLookup = mwsLookup;
      this.keepaLookup = keepaLookup;
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

   @RequestMapping(path="/keepam/{sku}")
   @ResponseBody public PasItemNode keepam(@PathVariable("sku") String sku) throws PricingException, ProductNotFoundException, ItemNotAccessibleException {
      return keepaLookup.lookup(sku, true);
   }
}
