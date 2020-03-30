package com.badals.shop.repository.projection;

import java.math.BigDecimal;

public interface PurchaseQueue {
   Long getId();
   String getProductName();
   BigDecimal getQuantity();
   BigDecimal getPrice();
   BigDecimal getCost();
   String getImage();
   BigDecimal getWeight();
   String getUrl();
   String getSku();
}
