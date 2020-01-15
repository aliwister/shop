package com.badals.shop.xtra.amazon.mappings;

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

    public Integer getTotalOffers() {
        return totalOffers;
    }

    public void setTotalOffers(Integer totalOffers) {
        this.totalOffers = totalOffers;
    }

    public Integer getTotalOfferPages() {
        return totalOfferPages;
    }

    public void setTotalOfferPages(Integer totalOfferPages) {
        this.totalOfferPages = totalOfferPages;
    }

    public String getMoreOffersUrl() {
        return moreOffersUrl;
    }

    public void setMoreOffersUrl(String moreOffersUrl) {
        this.moreOffersUrl = moreOffersUrl;
    }

    public OfferNode getOffer() {
        return offer;
    }

    public void setOffer(OfferNode offer) {
        this.offer = offer;
    }
}
