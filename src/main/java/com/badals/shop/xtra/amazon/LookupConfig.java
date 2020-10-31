package com.badals.shop.xtra.amazon;

import com.amazonservices.mws.products.MarketplaceWebServiceProductsClient;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsRequestHelper;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.ebay.EbayLookup;
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

   @Value("${pas.us.tag}")
   private String pasTag;

   @Value("${pas.us.host}")
   private String pasHost;

   @Value("${pas.us.region}")
   private String pasRegion;

   @Value("${pas.uk.tag}")
   private String pasUkTag;

   @Value("${pas.uk.host}")
   private String pasUkHost;

   @Value("${pas.uk.region}")
   private String pasUkRegion;


   @Value("${mws.key}")
   private String mwsAccessKeyId;

   @Value("${mws.secret}")
   private String mwsSecretKey;

   @Value("${mws.merchantid}")
   private String mwsMerchantId;

   @Value("${mws.marketplaceid}")
   private String mwsMarketPlaceId;

   @Value("${ebay.app-id}")
   private String ebayAppId;


   @Bean(name="us")
   public PasLookup pasLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

      System.out.println("======================================================"+pasAccessKeyId);

      final SignedRequestsHelper signedRequestsHelper = new SignedRequestsHelper(pasAccessKeyId, pasSecretKey);
      final PasLookup pasLookup = new PasLookup(pasAccessKeyId, pasSecretKey, pasTag, pasHost, pasRegion);
      return pasLookup;
   }

   @Bean(name="uk")
   public PasLookup pasUkLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

      System.out.println("======================================================"+pasAccessKeyId);

      final SignedRequestsHelper signedRequestsHelper = new SignedRequestsHelper(pasAccessKeyId, pasSecretKey);
      final PasLookup pasLookup = new PasLookup(pasAccessKeyId, pasSecretKey, pasUkTag, pasUkHost, pasUkRegion);
      return pasLookup;
   }

   @Bean
   public MwsLookup mwsLookup() throws InvalidKeyException, UnsupportedEncodingException, NoSuchAlgorithmException {

      System.out.println("======================================================"+pasAccessKeyId);
      //final MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient("AKIAIOAQ437ZZB7K4O2A", "6JS96EV8yvAcERdfy38Q2TzzCOwcfEYIHHZbricV");
      //MwsLookup mwsLookup = new MwsLookup(client);

      final MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient(mwsAccessKeyId, mwsSecretKey);
      final MwsLookup mwsLookup = new MwsLookup(client, mwsMerchantId, mwsMarketPlaceId );
      return mwsLookup;
   }

   @Bean
   public EbayLookup ebayLookup() {
      final EbayLookup ebayLookup = new EbayLookup(ebayAppId);
      return ebayLookup;
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
