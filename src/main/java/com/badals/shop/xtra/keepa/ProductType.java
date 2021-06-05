package com.badals.shop.xtra.keepa;

public enum ProductType {
   STANDARD(0),
   DOWNLOADABLE(1),
   EBOOK(2),
   INACCESSIBLE(3),
   INVALID(4),
   VARIATION_PARENT(5);

   private Integer code;

   ProductType(Integer code) {
      this.code = code;
   }
}
