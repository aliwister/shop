package com.badals.shop.xtra.amazon.mws;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
class OfferNode {
   public OfferNode(BigDecimal cost, boolean domestic, boolean prime, String sellerRating, int ratingCount, String maxShipping) {
      this.cost = cost;
      this.domestic = domestic;
      this.prime = prime;
      this.sellerRating = sellerRating;
      this.ratingCount = ratingCount;
      this.maxShipping = maxShipping;
   }

   BigDecimal cost;
   boolean domestic;
   boolean prime;

   String sellerRating;
   int ratingCount;

   String maxShipping;
}

@Data
public class MwsItemNode {
   List<OfferNode> offers = new ArrayList<>();

   public void addOffer(BigDecimal landedPrice, boolean domestic, boolean prime, String sellerPositiveFeedbackRating, int sellerFeedbackCount, String max) {
      offers.add(new OfferNode(landedPrice, domestic, prime, sellerPositiveFeedbackRating, sellerFeedbackCount, max));
   }
}
