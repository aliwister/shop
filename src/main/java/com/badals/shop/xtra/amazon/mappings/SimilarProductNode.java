package com.badals.shop.xtra.amazon.mappings;

import lombok.Data;

@Data
public class SimilarProductNode {

   String ASIN;
   String Title;

    public String getASIN() {
        return ASIN;
    }

    public void setASIN(String ASIN) {
        this.ASIN = ASIN;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }
}
