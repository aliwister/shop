package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerReviewsNode {
   String HasReviews;

    public String getHasReviews() {
        return HasReviews;
    }

    public void setHasReviews(String hasReviews) {
        HasReviews = hasReviews;
    }
}
