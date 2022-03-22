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
}
