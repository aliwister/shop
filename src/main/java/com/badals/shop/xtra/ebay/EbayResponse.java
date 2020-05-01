package com.badals.shop.xtra.ebay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbayResponse {
   @JsonProperty("Timestamp")
   String timestamp;
   @JsonProperty("Ack") String ack;
   @JsonProperty("Item") EbayItem item;
   @JsonProperty("ShippingCostSummary") EbayShippingCost shippingCost;

   @JsonProperty("Errors")
   List<EbayError> errors;

}
