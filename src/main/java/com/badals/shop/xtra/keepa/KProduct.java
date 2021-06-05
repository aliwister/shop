package com.badals.shop.xtra.keepa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data class CategoryTree {
   String catId;
   String name;
}

@Data class Variation {
   String asin;
   List<Attribute> attributes;
}

@Data class Attribute {
   String dimension;
   String value;
}

@Data class Promotion {
   String type;
   Integer amount;
   Integer discountPercent;
}

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KProduct {
   ProductType productType;
   String asin;
   Integer domainId;
   String title;
   Integer trackingSince;
   Integer listedSince;
   Integer lastUpdate;
   Integer lastRatingUpdate;
   Integer lastPriceChange;
   Integer lastEbayUpdate;
   String imagesCSV;
   Long rootCategory;
   List<Long> categories;
   List<CategoryTree> categoryTree;
   String parentAsin;
   String variationCSV;
   List<String> frequentlyBoughtTogether;
   List<String> eanList;
   List<String> upcList;
   String manufacturer;
   String brand;
   String productGroup;
   String partNumber;
   String author;
   String binding;
   Integer numberOfItems;
   Integer numberOfPages;
   Integer publicationDate;
   Integer releaseDate;
           //"languages":  two dimensional String array,
   String model;
   String color;

   String size;
   String edition;
   String format;
   List<String> features;
   String description;
   Integer packageHeight;
   Integer packageLength;
   Integer packageWidth;
   Integer packageWeight;
   Integer itemHeight;
   Integer itemLength;
   Integer itemWidth;
   Integer itemWeight;
   Integer availabilityAmazon;
   List<Integer> availabilityAmazonDelay;
   List<Long> ebayListingIds;
   Boolean isAdultProduct;
   Boolean launchpad;
   String audienceRating;
   Boolean newPriceIsMAP;
   Boolean isEligibleForTradeIn;
   Boolean isEligibleForSuperSaverShipping;
           //"fbaFees": Object,
   List<Variation> variations;
   List<Integer> coupon;
   List<Promotion> promotions;
           //"stats": Statistics Object,
   Long salesRankReference;
   Object salesRanks;
   String rentalDetails;
   String rentalSellerId;
           //"rentalPrices": Rental Object,
    //"offers": Marketplace Offer Object array,
   List<Integer> liveOffersOrder;
   List<String> buyBoxSellerIdHistory;
   Boolean isRedirectASIN;
   Boolean isSNS;
   Boolean offersSuccessful;
   Object csv;
}
