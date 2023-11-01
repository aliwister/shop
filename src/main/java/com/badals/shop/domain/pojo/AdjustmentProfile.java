package com.badals.shop.domain.pojo;

import com.badals.shop.domain.enumeration.DiscountReductionType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdjustmentProfile {
    PriceMap discount;
    DiscountReductionType discountReductionType;

    public AdjustmentProfile(PriceMap discount, DiscountReductionType discountReductionType) {
        this.discount = discount;
        this.discountReductionType = discountReductionType;
    }
}
