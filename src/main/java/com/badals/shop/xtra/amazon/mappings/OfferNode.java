package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class OfferNode {
   @XStreamAlias("OfferAttributes")
   OfferAttributesNode offerAttributes;

   @XStreamAlias("OfferListing")
   OfferListingNode offerListing;

    public OfferAttributesNode getOfferAttributes() {
        return offerAttributes;
    }

    public void setOfferAttributes(OfferAttributesNode offerAttributes) {
        this.offerAttributes = offerAttributes;
    }

    public OfferListingNode getOfferListing() {
        return offerListing;
    }

    public void setOfferListing(OfferListingNode offerListing) {
        this.offerListing = offerListing;
    }
}
