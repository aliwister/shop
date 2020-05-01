package com.badals.shop.xtra.ebay;

//import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.badals.shop.xtra.amazon.SignedRequestsHelper;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsRequestHelper;
import com.badals.shop.xtra.amazon.paapi4.Pas4Lookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

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
