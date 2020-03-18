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

package com.badals.shop.xtra.amazon.paapi5;

import com.amazon.paapi5.v1.*;
import com.amazon.paapi5.v1.api.DefaultApi;
import com.badals.shop.xtra.amazon.LookupConfig;
import com.badals.shop.xtra.amazon.SignedRequestsHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

//@Configuration
public class PasLookup {
   private final Logger log = LoggerFactory.getLogger(PasLookup.class);
   //@Autowired
   public SignedRequestsHelper helper;

   @Autowired
   private LookupConfig lookupConfig;

   private DefaultApi api;
   String partnerTag = "deseneoma-20";

   public PasLookup() {
      ApiClient client = new ApiClient();
      client.setAwsAccessKey("AKIAJH3HSRU4OMRAU5NA");
      // Please add your secret key here
      client.setAwsSecretKey("yu6R/dsS4DD+Ws3zZcAkmLqoMx4Nhg4Utcm5DCEO");

      // Enter your partner tag (store/tracking id)

      client.setHost("webservices.amazon.com");
      client.setRegion("us-east-1");
      this.api = new DefaultApi(client);
   }

   public PasLookup(SignedRequestsHelper helper) {
      this();
      this.helper = helper;
   }

   /*
    * Utility function to fetch the response from the service and extract the title
    * from the XML.
    */
   public GetItemsResponse lookup(List<String> itemIds) {
      GetItemsRequest getItemsRequest = new GetItemsRequest().itemIds(itemIds).partnerTag(partnerTag)
              .resources(getItemsResources()).partnerType(PartnerType.ASSOCIATES);
      try {
         return api.getItems(getItemsRequest);

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public GetVariationsResponse variationLookup(String asin, int page) {
      log.info("Calling variationLookup on page "+page);
      GetVariationsRequest getVariationsRequest = new GetVariationsRequest().ASIN(asin).partnerTag(partnerTag)
              .resources(getVariationsResources()).partnerType(PartnerType.ASSOCIATES).variationPage(page);
      try {
         return api.getVariations(getVariationsRequest);

      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }


   List<GetItemsResource> getItemsResources() {
      List<GetItemsResource> getItemsResources = new ArrayList<GetItemsResource>();
      getItemsResources.add(GetItemsResource.PARENTASIN);
      getItemsResources.add(GetItemsResource.ITEMINFO_TITLE);
      getItemsResources.add(GetItemsResource.ITEMINFO_FEATURES);
      getItemsResources.add(GetItemsResource.ITEMINFO_EXTERNALIDS);
      getItemsResources.add(GetItemsResource.ITEMINFO_MANUFACTUREINFO);

      getItemsResources.add(GetItemsResource.ITEMINFO_BYLINEINFO);
      getItemsResources.add(GetItemsResource.ITEMINFO_PRODUCTINFO);

      getItemsResources.add(GetItemsResource.IMAGES_PRIMARY_LARGE);
      getItemsResources.add(GetItemsResource.IMAGES_VARIANTS_LARGE);

      getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_AVAILABILITY_MINORDERQUANTITY);
      getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_AVAILABILITY_TYPE);
      getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_DELIVERYINFO_ISAMAZONFULFILLED);
      getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_DELIVERYINFO_ISPRIMEELIGIBLE);
      getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_DELIVERYINFO_SHIPPINGCHARGES);
      getItemsResources.add(GetItemsResource.OFFERS_LISTINGS_PRICE);
      getItemsResources.add(GetItemsResource.OFFERS_SUMMARIES_LOWESTPRICE);
      getItemsResources.add(GetItemsResource.OFFERS_SUMMARIES_OFFERCOUNT);

      return getItemsResources;
   }

   List<GetVariationsResource> getVariationsResources() {
      List<GetVariationsResource> GetVariationsResources = new ArrayList<GetVariationsResource>();
      GetVariationsResources.add(GetVariationsResource.PARENTASIN);
      GetVariationsResources.add(GetVariationsResource.ITEMINFO_TITLE);
      GetVariationsResources.add(GetVariationsResource.ITEMINFO_FEATURES);
      GetVariationsResources.add(GetVariationsResource.ITEMINFO_EXTERNALIDS);
      GetVariationsResources.add(GetVariationsResource.ITEMINFO_MANUFACTUREINFO);

      GetVariationsResources.add(GetVariationsResource.ITEMINFO_BYLINEINFO);
      GetVariationsResources.add(GetVariationsResource.ITEMINFO_PRODUCTINFO);

      GetVariationsResources.add(GetVariationsResource.IMAGES_PRIMARY_LARGE);
      GetVariationsResources.add(GetVariationsResource.IMAGES_VARIANTS_LARGE);
      GetVariationsResources.add(GetVariationsResource.OFFERS_LISTINGS_AVAILABILITY_MINORDERQUANTITY);
      GetVariationsResources.add(GetVariationsResource.OFFERS_LISTINGS_AVAILABILITY_TYPE);
      GetVariationsResources.add(GetVariationsResource.OFFERS_LISTINGS_DELIVERYINFO_ISAMAZONFULFILLED);
      GetVariationsResources.add(GetVariationsResource.OFFERS_LISTINGS_DELIVERYINFO_ISPRIMEELIGIBLE);
      GetVariationsResources.add(GetVariationsResource.OFFERS_LISTINGS_DELIVERYINFO_SHIPPINGCHARGES);
      GetVariationsResources.add(GetVariationsResource.OFFERS_LISTINGS_PRICE);
      GetVariationsResources.add(GetVariationsResource.OFFERS_SUMMARIES_LOWESTPRICE);
      GetVariationsResources.add(GetVariationsResource.OFFERS_SUMMARIES_OFFERCOUNT);
      GetVariationsResources.add(GetVariationsResource.VARIATIONSUMMARY_VARIATIONDIMENSION);



      return GetVariationsResources;
   }
}
