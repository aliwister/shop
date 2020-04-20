package com.badals.shop.xtra.amazon.mws;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.PasUtility;
import com.badals.shop.xtra.amazon.PricingException;

import java.math.BigDecimal;
import java.util.List;

public class MwsLookupParser {
   public static MerchantStock parseStock(MerchantStock stock, MwsItemNode i, BigDecimal weight, List<ProductOverride> overrides) throws PricingException, NoOfferException {
      // @Todo catch options with no offers

      OfferNode offer = selectOffer(i);

      Double dCost = offer.getCost().doubleValue();
      boolean isPrime = offer.isPrime();
      boolean isFulfilledByAmazon = offer.isPrime();
      String m = offer.getMaxShipping();
      Integer availability = getMaxHours(m) + 5*24;
      if(!isPrime) availability += 48;
      if(!isFulfilledByAmazon) availability+= 24;


      double margin = 5, risk = 2, fixed = 1.1;
      double localShipping = 0;

      BigDecimal price = PasUtility.calculatePrice(offer.getCost(), weight, localShipping, margin, risk, fixed, isPrime, isFulfilledByAmazon, null);
      return stock.store("Amazon.com").quantity(BigDecimal.valueOf(99)).cost(BigDecimal.valueOf(dCost)).availability(availability).allow_backorder(true).price(price).location("USA");
   }

   private static OfferNode selectOffer(MwsItemNode node) throws NoOfferException {
      double selOfferWeight = -10000000;
      if(node == null || node.getOffers() == null || node.getOffers().size() == 0)
         throw new NoOfferException("Item not available");
      double selCost = node.getOffers().get(0).cost.doubleValue();
      int offerSize = node.getOffers().size();
      OfferNode selOffer = null;//node.getOffers().get(0);

      for(OfferNode offer : node.getOffers()) {
         double cost = offer.getCost().doubleValue();
         double priceIndex = Math.min(Math.max((cost - selCost) * (selCost - cost) / selCost, selCost - cost), 1.5*(selCost - cost));
         if(Math.abs(selCost - cost) / selCost < 1)
            priceIndex *= Math.abs((selCost - cost) / selCost );
         String m = offer.getMaxShipping();
         Integer maxHours = getMaxHours(m);
         String r = offer.getSellerRating();
         Integer iRating = 0;
         Integer ratingCount = offer.getRatingCount();
         try { iRating = Integer.parseInt(r.substring(0,2)); } catch (NumberFormatException e) {/*Swallow*/}
         //if((ratingCount < offerSize || iRating < 50) &&!offer.isPrime() ) continue;
         double ratingCountIndex = ratingCount/ 5.0;
         if(ratingCount > 10) ratingCountIndex = 1 + Math.log10(Math.min(ratingCount,1000));
         if(iRating < 70) iRating = 0;
         double ratingIndex = (iRating-70)/2.0;
         double rIndex  = Math.max(ratingIndex,12) + ratingCountIndex + .5*ratingCountIndex*ratingIndex;
         double offerWeight = rIndex + priceIndex;
         if(offer.isPrime()) offerWeight += 11;
         else if(offer.isDomestic()) offerWeight += 5;

         if(offerWeight > selOfferWeight) {
            selOfferWeight = offerWeight;
            selOffer = offer;
         }
      }
      if(selOffer == null)
         throw new NoOfferException("Unable to bring this item as it is not available from any trusted sellers");

      return selOffer;
   }

   private static Integer getMaxHours(String m) {
      return Integer.parseInt(m.substring(m.indexOf('-')+1, m.indexOf(' ',m.indexOf('-'))))*24;
   }
}
