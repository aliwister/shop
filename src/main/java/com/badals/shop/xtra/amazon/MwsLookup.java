package com.badals.shop.xtra.amazon;
/*******************************************************************************
 * Copyright 2009-2013 Amazon Services. All Rights Reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * You may not use this file except in compliance with the License.
 * You may obtain a copy of the License at: http://aws.amazon.com/apache2.0
 * This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *******************************************************************************
 * Marketplace Web Service Products
 * API Version: 2011-10-01
 * Library Version: 2013-08-01
 * Generated: Wed Sep 25 16:54:41 GMT 2013
 */


import com.amazonservices.mws.client.*;
import com.amazonservices.mws.products.*;
import com.amazonservices.mws.products.model.*;
import org.w3c.dom.Node;

import java.util.List;


/** Sample call for GetLowestOfferListingsForASIN. */
public class MwsLookup {

    /**
     * Call the service, log response and exceptions.
     *
     * @param client
     * @param request
     *
     * @return The response.
     */

   private static final String SELLER_ID = "ARU2T5B3ROW0Z";
   private static final String MARKETPLACE_ID = "ATVPDKIKX0DER";
   private MarketplaceWebServiceProducts client;



    public MwsLookup(MarketplaceWebServiceProducts client) {
      super();
      // TODO Auto-generated constructor stub
      this.client = client;
   }

   public GetLowestOfferListingsForASINResponse invokeGetLowestOfferListingsForASIN(

            GetLowestOfferListingsForASINRequest request) {
        try {
            // Call the service.
            GetLowestOfferListingsForASINResponse response = client.getLowestOfferListingsForASIN(request);
            ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
            // We recommend logging every the request id and timestamp of every call.
            System.out.println("Response:");
            System.out.println("RequestId: "+rhmd.getRequestId());
            System.out.println("Timestamp: "+rhmd.getTimestamp());
            String responseXml = response.toXML();
            System.out.println(responseXml);
            return response;
        } catch (MarketplaceWebServiceProductsException ex) {
            // Exception properties are important for diagnostics.
            System.out.println("Service Exception:");
            ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
            if(rhmd != null) {
                System.out.println("RequestId: "+rhmd.getRequestId());
                System.out.println("Timestamp: "+rhmd.getTimestamp());
            }
            System.out.println("Message: "+ex.getMessage());
            System.out.println("StatusCode: "+ex.getStatusCode());
            System.out.println("ErrorCode: "+ex.getErrorCode());
            System.out.println("ErrorType: "+ex.getErrorType());
            throw ex;
        }
    }

    /**
     *  Command line entry point.
     */


    public GetMatchingProductResponse invokeGetMatchingProduct(

          GetMatchingProductRequest request) {
      try {
          // Call the service.
          GetMatchingProductResponse response = client.getMatchingProduct(request);
          ResponseHeaderMetadata rhmd = response.getResponseHeaderMetadata();
          // We recommend logging every the request id and timestamp of every call.
          System.out.println("Response:");
          System.out.println("RequestId: "+rhmd.getRequestId());
          System.out.println("Timestamp: "+rhmd.getTimestamp());
          String responseXml = response.toXML();
          System.out.println(responseXml);
          return response;
      } catch (MarketplaceWebServiceProductsException ex) {
          // Exception properties are important for diagnostics.
          System.out.println("Service Exception:");
          ResponseHeaderMetadata rhmd = ex.getResponseHeaderMetadata();
          if(rhmd != null) {
              System.out.println("RequestId: "+rhmd.getRequestId());
              System.out.println("Timestamp: "+rhmd.getTimestamp());
          }
          System.out.println("Message: "+ex.getMessage());
          System.out.println("StatusCode: "+ex.getStatusCode());
          System.out.println("ErrorCode: "+ex.getErrorCode());
          System.out.println("ErrorType: "+ex.getErrorType());
          throw ex;
      }
  }

    public Product getLowestOffer(String asin) {

        // Get a client connection.
        // Make sure you've set the variables in MarketplaceWebServiceProductsSampleConfig.
        //MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient();

        // Create a request.
        GetLowestOfferListingsForASINRequest request = new GetLowestOfferListingsForASINRequest();

        request.setSellerId(SELLER_ID);

        request.setMarketplaceId(MARKETPLACE_ID);
        ASINListType asinList = new ASINListType();

        asinList.withASIN(asin);

        request.setASINList(asinList);
        String itemCondition = "New";
        request.setItemCondition(itemCondition);
        Boolean excludeMe = Boolean.valueOf(true);
        request.setExcludeMe(excludeMe);

        // Make the call.
        GetLowestOfferListingsForASINResponse response = invokeGetLowestOfferListingsForASIN(request);
        return response.getGetLowestOfferListingsForASINResult().get(0).getProduct();
    }

    public Product getMatchingProduct (String asin) {

       // Get a client connection.
       // Make sure you've set the variables in MarketplaceWebServiceProductsSampleConfig.
       //MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient();

       // Create a request.
       GetMatchingProductRequest request = new GetMatchingProductRequest();

       request.setSellerId(SELLER_ID);

       request.setMarketplaceId(MARKETPLACE_ID);
       ASINListType asinList = new ASINListType();
       asinList.withASIN(asin);
       request.setASINList(asinList);

       // Make the call.
       GetMatchingProductResponse response =  invokeGetMatchingProduct(request);
       return response.getGetMatchingProductResult().get(0).getProduct();

   }


    public static void main(String[] args) {

       // Get a client connection.
       // Make sure you've set the variables in MarketplaceWebServiceProductsSampleConfig.
       MarketplaceWebServiceProductsClient client = MwsRequestHelper.getClient("AKIAIOAQ437ZZB7K4O2A", "6JS96EV8yvAcERdfy38Q2TzzCOwcfEYIHHZbricV");
       MwsLookup mwsLookup = new MwsLookup(client);

       // Create a request.
       GetLowestOfferListingsForASINRequest request = new GetLowestOfferListingsForASINRequest();
       String sellerId = "ARU2T5B3ROW0Z";
       request.setSellerId(sellerId);
       String marketplaceId = "ATVPDKIKX0DER";
       request.setMarketplaceId(marketplaceId);
       ASINListType asinList = new ASINListType();

       asinList.withASIN("B00BUAGTSQ");

       request.setASINList(asinList);
       String itemCondition = "New";
       request.setItemCondition(itemCondition);
       Boolean excludeMe = Boolean.valueOf(true);
       request.setExcludeMe(excludeMe);

       // Make the call.
       mwsLookup.invokeGetLowestOfferListingsForASIN(request);

   }


    public boolean printAmazonProduct(String asin) {
       com.amazonservices.mws.products.model.Product product;
       product = getMatchingProduct(asin);

       System.out.println("                Product");
       System.out.println();
       if (product.isSetIdentifiers()) {
           System.out.println("                    Identifiers");
           System.out.println();
           IdentifierType  identifiers = product.getIdentifiers();
           if (identifiers.isSetMarketplaceASIN()) {
               System.out.println("                        MarketplaceASIN");
               System.out.println();
               ASINIdentifier  marketplaceASIN = identifiers.getMarketplaceASIN();
               if (marketplaceASIN.isSetMarketplaceId()) {
                   System.out.println("                            MarketplaceId");
                   System.out.println();
                   System.out.println("                                " + marketplaceASIN.getMarketplaceId());
                   System.out.println();
               }
               if (marketplaceASIN.isSetASIN()) {
                   System.out.println("                            ASIN");
                   System.out.println();
                   System.out.println("                                " + marketplaceASIN.getASIN());
                   System.out.println();
               }
           }
           if (identifiers.isSetSKUIdentifier()) {
               System.out.println("                        SKUIdentifier");
               System.out.println();
               SellerSKUIdentifier  SKUIdentifier = identifiers.getSKUIdentifier();
               if (SKUIdentifier.isSetMarketplaceId()) {
                   System.out.println("                            MarketplaceId");
                   System.out.println();
                   System.out.println("                                " + SKUIdentifier.getMarketplaceId());
                   System.out.println();
               }
               if (SKUIdentifier.isSetSellerId()) {
                   System.out.println("                            SellerId");
                   System.out.println();
                   System.out.println("                                " + SKUIdentifier.getSellerId());
                   System.out.println();
               }
               if (SKUIdentifier.isSetSellerSKU()) {
                   System.out.println("                            SellerSKU");
                   System.out.println();
                   System.out.println("                                " + SKUIdentifier.getSellerSKU());
                   System.out.println();
               }
           }
       }
       if (product.isSetAttributeSets()) {
           System.out.println("                    Attributes");
           AttributeSetList attributeSetList = product.getAttributeSets();
           for (Object obj : attributeSetList.getAny()) {
               Node attribute = (Node) obj;
               if(attribute.hasChildNodes()) {

               }
               System.out.println(ProductsUtil.formatXml(attribute));
               System.out.println(attribute.getLocalName());

           }
           System.out.println();
       }
       if (product.isSetRelationships()) {
           System.out.println("                    Relationships");
           RelationshipList relationships = product.getRelationships();
           for (Object obj : relationships.getAny()) {
               Node relationship = (Node) obj;
               System.out.println(ProductsUtil.formatXml(relationship));
           }
           System.out.println();
       }
       if (product.isSetCompetitivePricing()) {
           System.out.println("                    CompetitivePricing");
           System.out.println();
           CompetitivePricingType  competitivePricing = product.getCompetitivePricing();
           if (competitivePricing.isSetCompetitivePrices()) {
               System.out.println("                        CompetitivePrices");
               System.out.println();
               CompetitivePriceList  competitivePrices = competitivePricing.getCompetitivePrices();
               List<CompetitivePriceType> competitivePriceList = competitivePrices.getCompetitivePrice();
               for (CompetitivePriceType competitivePrice : competitivePriceList) {
                   System.out.println("                            CompetitivePrice");
                   System.out.println();
               if (competitivePrice.isSetCondition()) {
                   System.out.println("                            condition");
                   System.out.println();
                   System.out.println("                                " + competitivePrice.getCondition());
                   System.out.println();
               }
               if (competitivePrice.isSetBelongsToRequester()) {
                   System.out.println("                            belongsToRequester");
                   System.out.println();
                   System.out.println("                                " + competitivePrice.isBelongsToRequester());
                   System.out.println();
               }
                   if (competitivePrice.isSetCompetitivePriceId()) {
                       System.out.println("                                CompetitivePriceId");
                       System.out.println();
                       System.out.println("                                    " + competitivePrice.getCompetitivePriceId());
                       System.out.println();
                   }
                   if (competitivePrice.isSetPrice()) {
                       System.out.println("                                Price");
                       System.out.println();
                       PriceType  price = competitivePrice.getPrice();
                       if (price.isSetLandedPrice()) {
                           System.out.println("                                    LandedPrice");
                           System.out.println();
                           MoneyType  landedPrice = price.getLandedPrice();
                           if (landedPrice.isSetCurrencyCode()) {
                               System.out.println("                                        CurrencyCode");
                               System.out.println();
                               System.out.println("                                            " + landedPrice.getCurrencyCode());
                               System.out.println();
                           }
                           if (landedPrice.isSetAmount()) {
                               System.out.println("                                        Amount");
                               System.out.println();
                               System.out.println("                                            " + landedPrice.getAmount());
                               System.out.println();
                           }
                       }
                       if (price.isSetListingPrice()) {
                           System.out.println("                                    ListingPrice");
                           System.out.println();
                           MoneyType  listingPrice = price.getListingPrice();
                           if (listingPrice.isSetCurrencyCode()) {
                               System.out.println("                                        CurrencyCode");
                               System.out.println();
                               System.out.println("                                            " + listingPrice.getCurrencyCode());
                               System.out.println();
                           }
                           if (listingPrice.isSetAmount()) {
                               System.out.println("                                        Amount");
                               System.out.println();
                               System.out.println("                                            " + listingPrice.getAmount());
                               System.out.println();
                           }
                       }
                       if (price.isSetShipping()) {
                           System.out.println("                                    Shipping");
                           System.out.println();
                           MoneyType  shipping = price.getShipping();
                           if (shipping.isSetCurrencyCode()) {
                               System.out.println("                                        CurrencyCode");
                               System.out.println();
                               System.out.println("                                            " + shipping.getCurrencyCode());
                               System.out.println();
                           }
                           if (shipping.isSetAmount()) {
                               System.out.println("                                        Amount");
                               System.out.println();
                               System.out.println("                                            " + shipping.getAmount());
                               System.out.println();
                           }
                       }
                   }
               }
           }
           if (competitivePricing.isSetNumberOfOfferListings()) {
               System.out.println("                        NumberOfOfferListings");
               System.out.println();
               NumberOfOfferListingsList  numberOfOfferListings = competitivePricing.getNumberOfOfferListings();
               List<OfferListingCountType> offerListingCountList = numberOfOfferListings.getOfferListingCount();
               for (OfferListingCountType offerListingCount : offerListingCountList) {
                   System.out.println("                            OfferListingCount");
                   System.out.println();
               if (offerListingCount.isSetCondition()) {
                   System.out.println("                            condition");
                   System.out.println();
                   System.out.println("                                " + offerListingCount.getCondition());
                   System.out.println();
               }
               if (offerListingCount.isSetValue()) {
                   System.out.println("                            Value");
                   System.out.println();
                   System.out.println("                                " + offerListingCount.getValue());
               }
               }
           }
           if (competitivePricing.isSetTradeInValue()) {
               System.out.println("                        TradeInValue");
               System.out.println();
               MoneyType  tradeInValue = competitivePricing.getTradeInValue();
               if (tradeInValue.isSetCurrencyCode()) {
                   System.out.println("                            CurrencyCode");
                   System.out.println();
                   System.out.println("                                " + tradeInValue.getCurrencyCode());
                   System.out.println();
               }
               if (tradeInValue.isSetAmount()) {
                   System.out.println("                            Amount");
                   System.out.println();
                   System.out.println("                                " + tradeInValue.getAmount());
                   System.out.println();
               }
           }
       }
       if (product.isSetSalesRankings()) {
           System.out.println("                    SalesRankings");
           System.out.println();
           SalesRankList  salesRankings = product.getSalesRankings();
           List<SalesRankType> salesRankList = salesRankings.getSalesRank();
           for (SalesRankType salesRank : salesRankList) {
               System.out.println("                        SalesRank");
               System.out.println();
               if (salesRank.isSetProductCategoryId()) {
                   System.out.println("                            ProductCategoryId");
                   System.out.println();
                   System.out.println("                                " + salesRank.getProductCategoryId());
                   System.out.println();
               }
               if (salesRank.isSetRank()) {
                   System.out.println("                            Rank");
                   System.out.println();
                   System.out.println("                                " + salesRank.getRank());
                   System.out.println();
               }
           }
       }
       if (product.isSetLowestOfferListings()) {
           System.out.println("                    LowestOfferListings");
           System.out.println();
           LowestOfferListingList  lowestOfferListings = product.getLowestOfferListings();
           List<LowestOfferListingType> lowestOfferListingList = lowestOfferListings.getLowestOfferListing();
           for (LowestOfferListingType lowestOfferListing : lowestOfferListingList) {
               System.out.println("                        LowestOfferListing");
               System.out.println();
               if (lowestOfferListing.isSetQualifiers()) {
                   System.out.println("                            Qualifiers");
                   System.out.println();
                   QualifiersType  qualifiers = lowestOfferListing.getQualifiers();
                   if (qualifiers.isSetItemCondition()) {
                       System.out.println("                                ItemCondition");
                       System.out.println();
                       System.out.println("                                    " + qualifiers.getItemCondition());
                       System.out.println();
                   }
                   if (qualifiers.isSetItemSubcondition()) {
                       System.out.println("                                ItemSubcondition");
                       System.out.println();
                       System.out.println("                                    " + qualifiers.getItemSubcondition());
                       System.out.println();
                   }
                   if (qualifiers.isSetFulfillmentChannel()) {
                       System.out.println("                                FulfillmentChannel");
                       System.out.println();
                       System.out.println("                                    " + qualifiers.getFulfillmentChannel());
                       System.out.println();
                   }
                   if (qualifiers.isSetShipsDomestically()) {
                       System.out.println("                                ShipsDomestically");
                       System.out.println();
                       System.out.println("                                    " + qualifiers.getShipsDomestically());
                       System.out.println();
                   }
                   if (qualifiers.isSetShippingTime()) {
                       System.out.println("                                ShippingTime");
                       System.out.println();
                       ShippingTimeType  shippingTime = qualifiers.getShippingTime();
                       if (shippingTime.isSetMax()) {
                           System.out.println("                                    Max");
                           System.out.println();
                           System.out.println("                                        " + shippingTime.getMax());
                           System.out.println();
                       }
                   }
                   if (qualifiers.isSetSellerPositiveFeedbackRating()) {
                       System.out.println("                                SellerPositiveFeedbackRating");
                       System.out.println();
                       System.out.println("                                    " + qualifiers.getSellerPositiveFeedbackRating());
                       System.out.println();
                   }
               }
               if (lowestOfferListing.isSetNumberOfOfferListingsConsidered()) {
                   System.out.println("                            NumberOfOfferListingsConsidered");
                   System.out.println();
                   System.out.println("                                " + lowestOfferListing.getNumberOfOfferListingsConsidered());
                   System.out.println();
               }
               if (lowestOfferListing.isSetSellerFeedbackCount()) {
                   System.out.println("                            SellerFeedbackCount");
                   System.out.println();
                   System.out.println("                                " + lowestOfferListing.getSellerFeedbackCount());
                   System.out.println();
               }
               if (lowestOfferListing.isSetPrice()) {
                   System.out.println("                            Price");
                   System.out.println();
                   PriceType  price1 = lowestOfferListing.getPrice();
                   if (price1.isSetLandedPrice()) {
                       System.out.println("                                LandedPrice");
                       System.out.println();
                       MoneyType  landedPrice1 = price1.getLandedPrice();
                       if (landedPrice1.isSetCurrencyCode()) {
                           System.out.println("                                    CurrencyCode");
                           System.out.println();
                           System.out.println("                                        " + landedPrice1.getCurrencyCode());
                           System.out.println();
                       }
                       if (landedPrice1.isSetAmount()) {
                           System.out.println("                                    Amount");
                           System.out.println();
                           System.out.println("                                        " + landedPrice1.getAmount());
                           System.out.println();
                       }
                   }
                   if (price1.isSetListingPrice()) {
                       System.out.println("                                ListingPrice");
                       System.out.println();
                       MoneyType  listingPrice1 = price1.getListingPrice();
                       if (listingPrice1.isSetCurrencyCode()) {
                           System.out.println("                                    CurrencyCode");
                           System.out.println();
                           System.out.println("                                        " + listingPrice1.getCurrencyCode());
                           System.out.println();
                       }
                       if (listingPrice1.isSetAmount()) {
                           System.out.println("                                    Amount");
                           System.out.println();
                           System.out.println("                                        " + listingPrice1.getAmount());
                           System.out.println();
                       }
                   }
                   if (price1.isSetShipping()) {
                       System.out.println("                                Shipping");
                       System.out.println();
                       MoneyType  shipping1 = price1.getShipping();
                       if (shipping1.isSetCurrencyCode()) {
                           System.out.println("                                    CurrencyCode");
                           System.out.println();
                           System.out.println("                                        " + shipping1.getCurrencyCode());
                           System.out.println();
                       }
                       if (shipping1.isSetAmount()) {
                           System.out.println("                                    Amount");
                           System.out.println();
                           System.out.println("                                        " + shipping1.getAmount());
                           System.out.println();
                       }
                   }
               }
               if (lowestOfferListing.isSetMultipleOffersAtLowestPrice()) {
                   System.out.println("                            MultipleOffersAtLowestPrice");
                   System.out.println();
                   System.out.println("                                " + lowestOfferListing.getMultipleOffersAtLowestPrice());
                   System.out.println();
               }
           }
       }
       return true;
    }
}
