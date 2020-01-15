package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OffersNode {
   @XStreamAlias("TotalOffers")
   public Integer totalOffers;
   
   @XStreamAlias("TotalOfferPages")
   public Integer totalOfferPages;
   
   @XStreamAlias("MoreOffersUrl")
   public String moreOffersUrl;
   
   @XStreamAlias("Offer")
   OfferNode offer;
}
