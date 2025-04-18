package com.badals.shop.domain.pojo;

import com.badals.shop.domain.enumeration.DiscountReductionType;
import com.badals.shop.domain.enumeration.DiscountSource;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class CheckoutOrderAdjustment implements Serializable {
    PriceMap discount;
    DiscountReductionType discountReductionType;
    DiscountSource discountSource;
    String sourceRef;

    public CheckoutOrderAdjustment(PriceMap discount, DiscountReductionType discountReductionType) {
        this.discount = discount;
        this.discountReductionType = discountReductionType;
    }

    public CheckoutOrderAdjustment(PriceMap discount, DiscountReductionType discountReductionType, DiscountSource discountSource, String sourceRef) {
        this.discount = discount;
        this.discountReductionType = discountReductionType;
        this.discountSource = discountSource;
        this.sourceRef = sourceRef;
    }
}
