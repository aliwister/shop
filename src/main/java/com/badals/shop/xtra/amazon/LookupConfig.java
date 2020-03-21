package com.badals.shop.xtra.amazon;

//import com.amazonservices.mws.products.MarketplaceWebServiceProductsAsyncClient;
import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsRequestHelper;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class LookupConfig {

   @Value("${pas.key}")
   private String pasAccessKeyId;

   @Value("${pas.secret}")
   private String pasSecretKey;

   @Value("${mws.key}")
   private String mwsAccessKeyId;

   @Value("${mws.secret}")
   private String mwsSecretKey;

   @Value("${mws.merchantid}")
   private String mwsMerchantId;

   @Value("${mws.marketplaceid}")
   private String mwsMarketPlaceId;


   @Bean
   public PasLookup pasLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

      System.out.println("======================================================"+pasAccessKeyId);

      final SignedRequestsHelper signedRequestsHelper = new SignedRequestsHelper(pasAccessKeyId, pasSecretKey);
      final PasLookup pasLookup = new PasLookup(signedRequestsHelper);
      return pasLookup;
   }

   @Bean
   public MwsLookup mwsLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

      System.out.println("======================================================"+pasAccessKeyId);
      //final MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient("***REMOVED***", "***REMOVED***");
      //MwsLookup mwsLookup = new MwsLookup(client);

      final MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient(mwsAccessKeyId, mwsSecretKey);
      final MwsLookup mwsLookup = new MwsLookup(client, mwsMerchantId, mwsMarketPlaceId );
      return mwsLookup;
   }





   /*
   @Bean
   public MwsLookup mwsLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {
      System.out.println("======================================================"+mwsAccessKeyId);

      final MarketplaceWebServiceProductsAsyncClient signedRequestsHelper = MwsRequestHelper.getAsyncClient(mwsAccessKeyId, mwsSecretKey);
      final MwsLookup mwsLookup = new MwsLookup(signedRequestsHelper);
      return mwsLookup;
   }

    */

}
