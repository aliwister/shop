/**********************************************************************************************
 * Copyright 2009 Amazon.com, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file 
 * except in compliance with the License. A copy of the License is located at
 *
 *       http://aws.amazon.com/apache2.0/
 *
 * or in the "LICENSE.txt" file accompanying this file. This file is distributed on an "AS IS"
 * BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under the License. 
 *
 * ********************************************************************************************
 *
 *  Amazon Product Advertising API
 *  Signed Requests Sample Code
 *
 *  API Version: 2009-03-31
 *
 */

package com.badals.shop.vendor.amazon;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.badals.shop.vendor.amazon.pas.mappings.AncestorsNode;
import com.badals.shop.vendor.amazon.pas.mappings.BrowseNodeNode;
import com.badals.shop.vendor.amazon.pas.mappings.BrowseNodesNode;
import com.badals.shop.vendor.amazon.pas.mappings.DimensionsNode;
import com.badals.shop.vendor.amazon.pas.mappings.EditorialReviewNode;
import com.badals.shop.vendor.amazon.pas.mappings.ImageSetNode;
import com.badals.shop.vendor.amazon.pas.mappings.ItemAttributesNode;
import com.badals.shop.vendor.amazon.pas.mappings.ItemLookupRequestNode;
import com.badals.shop.vendor.amazon.pas.mappings.ItemLookupResponse;
import com.badals.shop.vendor.amazon.pas.mappings.ItemNode;
import com.badals.shop.vendor.amazon.pas.mappings.ItemsNode;
import com.badals.shop.vendor.amazon.pas.mappings.OfferAttributesNode;
import com.badals.shop.vendor.amazon.pas.mappings.OfferListingNode;
import com.badals.shop.vendor.amazon.pas.mappings.OfferNode;
import com.badals.shop.vendor.amazon.pas.mappings.OffersNode;
import com.badals.shop.vendor.amazon.pas.mappings.PriceNode;
import com.badals.shop.vendor.amazon.pas.mappings.RequestNode;
import com.badals.shop.vendor.amazon.pas.mappings.SimilarProductNode;
import com.badals.shop.vendor.amazon.pas.mappings.VariationAttributeNode;
import com.badals.shop.vendor.amazon.pas.mappings.VariationsNode;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

//@Configuration
public class PasLookup {

   //@Autowired
   public SignedRequestsHelper helper;

   private XStream xstream;
   
   public PasLookup() {
      xstream = new XStream(new StaxDriver());
      xstream.ignoreUnknownElements();
      xstream.alias("ItemLookupResponse", ItemLookupResponse.class);
      xstream.alias("Items", ItemsNode.class);
      xstream.alias("Request", RequestNode.class);
      xstream.alias("ItemLookupRequest", ItemLookupRequestNode.class);
      xstream.addImplicitCollection(ItemLookupRequestNode.class, "ResponseGroups",  "ResponseGroup", String.class);
      
      
      xstream.alias("Item", ItemNode.class);
      xstream.addImplicitCollection(ItemsNode.class, "items", "Item", ItemNode.class);
      xstream.alias("ImageSet", ImageSetNode.class);
      
      xstream.alias("VariationDimension", String.class);
      xstream.addImplicitCollection(VariationsNode.class, "items", "Item", ItemNode.class);
      
      xstream.alias("EditorialReview", EditorialReviewNode.class);
      xstream.alias("SimilarProduct", SimilarProductNode.class);
      xstream.alias("VariationAttribute", VariationAttributeNode.class);
      
      Class [] annotClasses = {ItemNode.class, ImageSetNode.class, ItemAttributesNode.class, DimensionsNode.class, OffersNode.class, OfferNode.class, OfferAttributesNode.class, OfferListingNode.class, PriceNode.class, VariationsNode.class, BrowseNodesNode.class, BrowseNodeNode.class, AncestorsNode.class};
      xstream.processAnnotations(annotClasses);
   }
   
   public PasLookup(SignedRequestsHelper helper) {
      this();
      this.helper = helper;
   }

   public ItemLookupResponse lookup(String asin) {
      String requestUrl = null;

      Map<String, String> params = new HashMap<String, String>();
      params.put("Service", "AWSECommerceService");
      params.put("Version", "2010-06-01");
      params.put("Operation", "ItemLookup");
      params.put("ItemId", asin);
      params.put("ResponseGroup", "BrowseNodes,VariationMatrix,VariationOffers,VariationImages,OfferListings,ItemAttributes,Images,Similarities,EditorialReview,Reviews");
      params.put("AssociateTag", "deseneoma-20");

      requestUrl = helper.sign(params);
      System.out.println("Signed Request is \"" + requestUrl + "\"");
      return fetch(requestUrl);
   }
   
   /*
    * Utility function to fetch the response from the service and extract the title
    * from the XML.
    */
   private ItemLookupResponse fetch(String requestUrl) {
      ItemLookupResponse doc = null;
      try {
      	//File file = new File("C:\\work\\face\\face-boot\\shop\\src\\main\\resources\\Response.txt");
         //URL req = file.toURI().toURL(); // 
         URL req = new URL(requestUrl);

         xstream.setClassLoader(Thread.currentThread().getContextClassLoader());// does not require XPP3 library starting with Java 6
         doc = (ItemLookupResponse) xstream.fromXML(req.openStream());
         return doc;
   
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }
}
