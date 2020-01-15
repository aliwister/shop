package com.badals.shop.vendor.amazon.pas.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;


@Data
public class OfferListingNode {
   @XStreamAlias("OfferListingId")
   String offerListingId;
   
   @XStreamAlias("Price")
   PriceNode price;
   
   @XStreamAlias("Availability")
   String availability;
   
   @XStreamAlias("AvailabilityAttributes")
   AvailabilityAttributesNode availabilityAttributesNode;
   
   @XStreamAlias("IsEligibleForSuperSaverShipping")
   String isEligibleForSuperSaverShipping;
   
   @XStreamAlias("IsEligibleForPrime")
   String isEligibleForPrime;
}
