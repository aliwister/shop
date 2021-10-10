package com.badals.shop.xtra.ebay;

import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.PasItemNode;

import com.badals.shop.xtra.amazon.PricingException;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
   private String APP_ID;
   private final String lookupUri;
   private final String lookupShippingUri;

   public EbayLookup(String ebayAppId) {
      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      converter.setSupportedMediaTypes(Arrays.asList(TEXT_PLAIN, APPLICATION_JSON));
      restTemplate = new RestTemplateBuilder(rt-> rt.getInterceptors().add((request, body, execution) -> {
         request.getHeaders().add("X-EBAY-API-IAF-TOKEN", APP_ID);
         return execution.execute(request, body);
      })).build();
      restTemplate.getMessageConverters().add(converter);
      APP_ID = ebayAppId;
      lookupUri = "https://open.api.ebay.com/shopping?callname=GetSingleItem&version=975&responseencoding=JSON&ItemID=";
      lookupShippingUri = "https://open.api.ebay.com/shopping?callname=GetShippingCosts&version=975&responseencoding=JSON&ItemID=";
   }

   public PasItemNode lookup(String id) throws ProductNotFoundException, PricingException {
      EbayResponse response =  restTemplate.getForObject(lookupUri+id,EbayResponse.class);
      if(response != null && response.getItem() != null) {
         EbayItem item = response.getItem();
         if(!item.getListingStatus().equals("Active"))
            throw new ProductNotFoundException("Not active");

         PasItemNode node = new PasItemNode();
         node.setId(item.getItemID());
         node.setImage(item.getGallery().get(0));
         node.setCost(item.getPrice().getValue());
         node.setPrime(item.getCountry().equalsIgnoreCase("US"));
         node.setShippingCountry(item.getCountry());
         node.setTitle(item.getTitle());
         node.setAvailabilityType("Now");

         node.setVariationType(VariationType.SIMPLE);

         //EbayResponse omShipping = restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode=OM",EbayResponse.class);
         EbayResponse usShipping = restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode=US&DestinationPostalCode=34249",EbayResponse.class);
         EbayResponse omShipping = restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode=OM",EbayResponse.class);

         if(usShipping!= null && usShipping.getShippingCost() != null )
            node.setShippingCharges(usShipping.getShippingCost().getCost().getValue());

         if(omShipping!= null && omShipping.getShippingCost() != null )
            node.setOmanShippingCharges(omShipping.getShippingCost().getCost().getValue());

         if(node.getOmanShippingCharges() == null && node.getShippingCharges() == null)
            throw new PricingException("Unable to support this item");

         return node;
      }
      throw new ProductNotFoundException("Unable to parse item");
   }

   public EbayResponse lookupShipping(String id, String destination) {
      return restTemplate.getForObject(lookupShippingUri+id+"&DestinationCountryCode="+destination,EbayResponse.class);
   }

   public static void main (String args[]) throws PricingException, ProductNotFoundException {
      EbayLookup service = new EbayLookup("v^1.1#i^1#r^0#f^0#p^1#I^3#t^H4sIAAAAAAAAAOVYf2wTVRxfu24EoRCUiU7EcghR9K53115/XGi1o0yW/erWbrJNxXd378ax691x75W1kpi5P1BjNGrgD8Qf/BJNUCMh6EDU8AcRJSQGTVCJBokhGEFCQDAxinddGd0kgLQxS2ybNO/7vu/7Pp/P+37fe3f0QPXE+asXr77gdkxwbhigB5wOBzOJnlhddd+USmdtVQVd5ODYMHD3gGuw8sQCBNKqwbdDZOgagp5sWtUQnzdGiIyp8TpACuI1kIaIxyKfjDU38SxF84apY13UVcLTEI8QgRDk/BwI+oSwxIrBgGXVLsVM6RGCYYOSyIpyOAzCIMAwVj9CGdigIQw0HCFYmmVIhrZ+KYbmfTTPcRQTCHQTnk5oIkXXLBeKJqJ5uHx+rFmE9epQAULQxFYQItoQq0+2xhrii1pSC7xFsaIFHZIY4Awa3VqoS9DTCdQMvPo0KO/NJzOiCBEivNHhGUYH5WOXwNwA/LzUYZllYIAOi4BjABP0lUXKet1MA3x1HLZFkUg578pDDSs4dy1FLTWE5VDEhVaLFaIh7rH/2jJAVWQFmhFiUV2sK5ZIENE6IAE1ZQKSk/ws6eckSAp0WCC5UECWGb8AuaBcmGQ4UkHiMbMs1DVJsQVDnhYd10ELMRyrC1uki+XUqrWaMRnbaIr92BH9mG57QYdXMIOXafaawrQlgiffvLb6I6MxNhUhg+FIhLEdeXkiBDAMRSLGdubzsJA6WRQhlmFs8F5vf38/1e+jdLPXy9I0413S3JQUl8E0IAq+dq1nkXLtAaSSpyJCayRSeJwzLCxZK08tAFovEQ2HaCZYkH00quhY6z8MRZS9o4uhXMURFIIwJISCMsMKMheG5SiOaCE/vTYOKIAcmQZmH8SGCkRIilaaZdLQVCTex8msLyRDUgqEZdIflmVS4KQAycgQ0hAKghgO/V9q5HqzPAlFE+KypXlZUjyXkeK9YrAlF2uJL86yoY4VQGzK+psTi1BHggUhASXrkv7GvodiDZHrrYQrkl+oKpYyKWv+sgpg13rJIizWEYZSSfSSom7AhK4qYm58LbDPlBLAxLkkVFXLUBLJmGE0lG+bLgu9f7FF3Bjn8h5N//2xdEVSyM7W8UXKHo+sAMBQKPvgoUQ97dWBdeGwTUttxHatw5J4K9ZddVyxtkgOs1Wk4UsmladMoZUiZUKkZ0zrfk212veulN4HNesow6auqtDsZEou5XQ6g4GgwvFW06XntwLG2THLBHy+UIAJcVxJvMT8Ibp0vG1I5dyECwaX/3ou0t7RT/TRivyHGXTspAcd250OB+2l5zJz6NnVlR2uysm1SMGQUoBMIaVXsx5UTUj1wZwBFNNZ7eifsfvNj4veIWx4lL5t5C3CxEpmUtErBXrm5Z4qZuoMN8swtP310RzXTc+53OtibnVNP137wEtL6r9oe8wJ63esqWM6975zC+0ecXI4qipcg44KfKTrw5+0nT3b9h9+bs/G7uNx9/roE66Zp1ad7uvtmb/lxTu8m/d/EH/44KzJ07JPPv+t2/iz0f3+Jvnw7OYdh9r+WrHmLOyZ95256+Xva8iai+rvB4ZWuU5cOLtx6h7Xufk9rw80cu4jF/c5Fww9m4h89us9J7ete2GoIvGDWvVg3SYcb/xo19f3zjzcvrf7kaP7zmw59sCeIX557dtnzh569yhv5moOAO6NfRMSnSvbX70/857ntc031Zx86ty6zlmc+/am7JFtU9/yfvn4PGLX2p/bzh/86o8Vn7+ydrd017kff5k+jf10StedF7s2zV29td1/8/GOk9ulec+c4np/e3ra+r7zx/TUJ1v3a/XHvxlevr8BJR80Ud0RAAA=");
      PasItemNode node = service.lookup("174922268405");
      System.out.println(node);
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
