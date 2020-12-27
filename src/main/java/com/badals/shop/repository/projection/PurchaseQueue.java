package com.badals.shop.repository.projection;

import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.util.List;

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
   Long getOrderId();
   Long getProductId();
   Long getMerchantId();

   //@Value("T(org.springframework.util.CollectionUtils).arrayToList(attributes.split(','))")
   String getAttributes();
}
