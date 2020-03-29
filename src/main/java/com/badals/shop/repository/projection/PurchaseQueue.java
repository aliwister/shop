package com.badals.shop.repository.projection;

import java.math.BigDecimal;

public interface PurchaseQueue {
   Long getId();
   String getProductName();
   BigDecimal getQuantity();
   BigDecimal getPrice();
   String getImage();
   BigDecimal getWeight();
   String getUrl();
   String getSku();
}
