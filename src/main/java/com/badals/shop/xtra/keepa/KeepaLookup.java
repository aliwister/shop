package com.badals.shop.xtra.keepa;

import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.ItemNotAccessibleException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PasItemNode;
import com.badals.shop.xtra.amazon.PricingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.TEXT_PLAIN;

@Component
public class KeepaLookup {

   private final RestTemplate restTemplate;
   private final KeepaMapper keepaMapper;
   private final String APP_ID;
   private final String lookupUri;

   private final Logger log = LoggerFactory.getLogger(KeepaLookup.class);

   public KeepaLookup(KeepaMapper keepaMapper) {
      this.keepaMapper = keepaMapper;
      MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
      converter.setSupportedMediaTypes(Arrays.asList(TEXT_PLAIN, APPLICATION_JSON));
      restTemplate = new RestTemplate();
      restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
      restTemplate.getMessageConverters().add(converter);
      APP_ID = "2fk5l4evnaer7h24jrn18ahu1vpk5o1dv692mdnmdqat1uuh8j9kn5l44mjismia";
      lookupUri = "https://api.keepa.com/product?key="+APP_ID+"&domain=1&history=1&days=1&asin="; //B0928WTNY1";
   }

   public PasItemNode lookup(String id, Boolean isRating) throws ProductNotFoundException, PricingException, ItemNotAccessibleException {
      String url = lookupUri+id + (isRating?"&rating=1":"&rating=0");
      log.info(url);
      KeepaResponse response =  restTemplate.getForObject(url,KeepaResponse.class);
      log.info("keepa response");
      if (response.getProducts() == null || response.getProducts().size() == 0)
         throw new PricingException("Invalid API Response");

      KProduct product = response.getProducts().get(0);
      if(product.getProductType().equals(ProductType.INACCESSIBLE))
         throw new ItemNotAccessibleException("This item is not accessible using Keepa");

      if(product.getProductType().equals(ProductType.INVALID))
         throw new ProductNotFoundException("This item is invalid");

      log.info("mapping to pasitemnode");
      PasItemNode node = keepaMapper.itemToPasItemNode(product);
      log.info("done mapping to pasitemnode");
      product = null;
      response = null;

      return node;
   }


   public static void main (String args[]) throws PricingException, ProductNotFoundException, ItemNotAccessibleException {
      KeepaLookup lookup = new KeepaLookup(null);
      PasItemNode response =  lookup.lookup("B0928WTNY1", true);
      //KProduct product = response.getProducts().get(0);
      //List<List<Integer>> csv = (List<List<Integer>>) product.getCsv();
      //System.out.println(response.processingTimeInMs);
      //https://stackoverflow.com/questions/64359760/illegal-character-ctrl-char-code-0-parsing-exception   }
   }
}
