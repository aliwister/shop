package com.badals.shop.xtra.amazon.mappings;

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

    public String getOfferListingId() {
        return offerListingId;
    }

    public void setOfferListingId(String offerListingId) {
        this.offerListingId = offerListingId;
    }

    public PriceNode getPrice() {
        return price;
    }

    public void setPrice(PriceNode price) {
        this.price = price;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public AvailabilityAttributesNode getAvailabilityAttributesNode() {
        return availabilityAttributesNode;
    }

    public void setAvailabilityAttributesNode(AvailabilityAttributesNode availabilityAttributesNode) {
        this.availabilityAttributesNode = availabilityAttributesNode;
    }

    public String getIsEligibleForSuperSaverShipping() {
        return isEligibleForSuperSaverShipping;
    }

    public void setIsEligibleForSuperSaverShipping(String isEligibleForSuperSaverShipping) {
        this.isEligibleForSuperSaverShipping = isEligibleForSuperSaverShipping;
    }

    public String getIsEligibleForPrime() {
        return isEligibleForPrime;
    }

    public void setIsEligibleForPrime(String isEligibleForPrime) {
        this.isEligibleForPrime = isEligibleForPrime;
    }
}
