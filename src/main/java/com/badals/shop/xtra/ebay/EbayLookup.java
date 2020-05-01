package com.badals.shop.xtra.ebay;

import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.PasItemNode;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;
import static org.apache.cxf.message.Message.PROTOCOL_HEADERS;
import static org.apache.cxf.phase.Phase.PRE_PROTOCOL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;


public class EbayLookup {

   private final RestTemplate restTemplate;
   private final String APP_ID;
   private final String lookupUri;
   private final String lookupShippingUri;

   public EbayLookup(String ebayAppId) {
      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      converter.setSupportedMediaTypes(Arrays.asList(TEXT_PLAIN, APPLICATION_JSON));
      restTemplate = new RestTemplate();
      restTemplate.getMessageConverters().add(converter);
      APP_ID = ebayAppId;
      lookupUri = "http://open.api.ebay.com/shopping?callname=GetSingleItem&version=975&siteid=0&responseencoding=JSON&appid="+APP_ID+"&ItemID=";
      lookupShippingUri = "http://open.api.ebay.com/shopping?callname=GetShippingCosts&version=975&siteid=0&responseencoding=JSON&appid="+APP_ID+"&ItemID=";
   }

   public PasItemNode lookup(String id) throws ProductNotFoundException {
      EbayResponse response =  restTemplate.getForObject(lookupUri+id,EbayResponse.class);
      if(response != null && response.getItem() != null) {
         EbayItem item = response.getItem();
         if(!item.getListingStatus().equals("Active"))
            throw new ProductNotFoundException("Not active");

         PasItemNode node = new PasItemNode();
         node.setId(item.getItemID());
         node.setImage(item.getGallery().get(0));
         node.setCost(item.getPrice().getValue());
         node.setPrime(item.getCountry().equals("US"));
         node.setTitle(item.getTitle());
         node.setAvailabilityType("Now");

         node.setVariationType(VariationType.SIMPLE);

         //EbayResponse omShipping = restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode=OM",EbayResponse.class);
         EbayResponse usShipping = restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode=US&DestinationPostalCode=34249",EbayResponse.class);

         node.setShippingCharges(usShipping.getShippingCost().getCost().getValue());
         //node.setOmanShippingCharges(omShipping)

         return node;
      }
      throw new ProductNotFoundException("Unable to parse item");
   }

   public EbayResponse lookupShipping(String id, String destination) {
      return restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode="+destination,EbayResponse.class);
   }

   public static void main (String args[]) {
/*      EbayLookup service = new EbayLookup();
      //ShoppingInterface api = service.buildShoppingApiService();
      SimpleItemType item = service.getEbayItem("133296152354");
      System.out.println(item);*/
  /*    String APP_ID = "BadalTra-5d42-45de-b09b-586ff14be57f";
      // Item
      final String uri1 = "http://open.api.ebay.com/shopping?callname=GetSingleItem&version=975&siteid=0&responseencoding=JSON&appid="+APP_ID+"&ItemID="+"133296152354";
      EbayResponse  response1 = restTemplate.getForObject(uri1,EbayResponse.class);
      System.out.println(response1);

      // Shipping Oman - Error
      final String uri2 = "http://open.api.ebay.com/shopping?callname=GetSingleItem&version=975&siteid=0&responseencoding=JSON&appid="+APP_ID+"&ItemID="+"133296152354"+"&DestinationCountryCode=OM";
      EbayResponse response2 = restTemplate.getForObject(uri2,EbayResponse.class);
      System.out.println(response2);

      // Shipping to US
      final String uri3 = "http://open.api.ebay.com/shopping?callname=GetSingleItem&version=975&siteid=0&responseencoding=JSON&appid="+APP_ID+"&ItemID="+"133296152354"+"&DestinationCountryCode=US&DestinationPostalCode=34249";
      EbayResponse response3 = restTemplate.getForObject(uri3,EbayResponse.class);
      System.out.println(response3);

      // Shipping Oman - Success
      final String uri4 = "http://open.api.ebay.com/shopping?callname=GetSingleItem&version=975&siteid=0&responseencoding=JSON&appid="+APP_ID+"&ItemID="+"202681472442"+"&DestinationCountryCode=OM";
      EbayResponse response4 = restTemplate.getForObject(uri4,EbayResponse.class);
      System.out.println(response4);*/
   }
}
