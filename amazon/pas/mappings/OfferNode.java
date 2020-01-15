package com.badals.shop.vendor.amazon.pas.mappings;

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
}
