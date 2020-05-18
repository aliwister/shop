package com.badals.shop.xtra.ebay;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EbayLookupConfig {

   @Value("${ebay.app-id}")
   private String appId;


/*   @Bean
   public PasLookup ebayLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

      System.out.println("======================================================" + appId);

      final SignedRequestsHelper signedRequestsHelper = new SignedRequestsHelper(pasAccessKeyId, pasSecretKey);
      final PasLookup pasLookup = new PasLookup(signedRequestsHelper);
      return pasLookup;
   }*/
}
