package com.badals.shop.xtra.ebay;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;


@Data
class EbayPrice {
   @JsonProperty("Value")
   BigDecimal value;
   @JsonProperty("Currency") String currency;
}

@Data
class EbayShippingCost {
   @JsonProperty("ShippingServiceName") String carrier;
   @JsonProperty("ShippingServiceCost") EbayPrice cost;
}

@Data
class EbayError {
   @JsonProperty("ShortMessage") String shortMessage;
   @JsonProperty("LongMessage") String longMessage;
   @JsonProperty("ErrorCode") String errorCode;
   @JsonProperty("SeverityCode") String severityCode;
   @JsonProperty("ErrorClassification") String errorClassification;
}


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EbayItem {
   @JsonProperty("ItemID") String itemID;
   @JsonProperty("Name") String name;
   @JsonProperty("ListingType") String listingType;
   @JsonProperty("ViewItemURLForNaturalSearch") String url;
   @JsonProperty("Location") String location;
   @JsonProperty("GalleryURL") String image;
   @JsonProperty("ListingStatus") String listingStatus;
   @JsonProperty("Title") String title;
   @JsonProperty("Country") String country;
   @JsonProperty("ConditionDisplayName") String condition;
   @JsonProperty("GlobalShipping") String globalShipping;
   @JsonProperty("ConvertedCurrentPrice") EbayPrice price;
   @JsonProperty("PictureURL")
   List<String> gallery;
}
