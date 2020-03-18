package com.badals.shop.repository.projection;

import java.math.BigDecimal;

public interface CartItemInfo {
   BigDecimal getPrice();
   Long getRef();
   BigDecimal getQuantity();
   String getImage();
   String getTitle();
   BigDecimal getWeight();

}
