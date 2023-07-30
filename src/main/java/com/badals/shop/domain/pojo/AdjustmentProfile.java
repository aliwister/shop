package com.badals.shop.domain.pojo;

import com.badals.shop.domain.enumeration.DiscountReductionType;
import lombok.Data;

@Data
public class AdjustmentProfile {
   PriceMap discount;
   DiscountReductionType type;

   public AdjustmentProfile(PriceMap discount, DiscountReductionType reductionType) {
      this.discount = discount;
      this.type = reductionType;
   }
}