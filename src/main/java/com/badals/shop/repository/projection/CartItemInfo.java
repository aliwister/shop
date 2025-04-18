package com.badals.shop.repository.projection;

import java.math.BigDecimal;

public interface CartItemInfo {
   BigDecimal getPrice();
   BigDecimal getBasePrice();

   BigDecimal getCost();
   String getBaseCurrency();
   Long getRef();
   BigDecimal getQuantity();
   String getImage();
   String getTitle();
   BigDecimal getWeight();
   String getSku();
   Long getAvailability();
   Long getMerchantId();
}
