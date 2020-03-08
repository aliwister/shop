package com.badals.shop.service.pojo;

import lombok.Data;

@Data
public class CheckoutSession {
   String redirectUrl;
   String secureKey;

   public CheckoutSession(String redirectUrl, String secureKey) {
      this.redirectUrl = redirectUrl;
      this.secureKey = secureKey;
   }
}
