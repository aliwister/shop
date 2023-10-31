package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

enum OrderAdjustmentType {
   DISCOUNT, FEE, SHIPPING, HANDLING, COMMISSION;
}

@Data
public class OrderAdjustment implements Serializable {
   OrderAdjustmentType type;
   String description;
   Integer quantity;
   BigDecimal value;

   public OrderAdjustment() {
   }

   public void setShippingValueAndType(BigDecimal value, String description) {
      this.value = value;
      this.description = description;
      this.type = OrderAdjustmentType.SHIPPING;
      this.quantity = 1;
   }

   public OrderAdjustment(OrderAdjustmentType type, String description, Integer quantity, BigDecimal value) {
      this.type = type;
      this.description = description;
      this.quantity = quantity;
      this.value = value;
   }

//   public static OrderAdjustment discount(String description, BigDecimal value) {
//      return new OrderAdjustment(OrderAdjustmentType.DISCOUNT, description, 1, value);
//   }
//   public static OrderAdjustment delivery(String description, BigDecimal value) {
//      return new OrderAdjustment(OrderAdjustmentType.SHIPPING, description, 1, value);
//   }

   public boolean shippingCheck() {
      return this.getType().equals(OrderAdjustmentType.SHIPPING);
   }
}
