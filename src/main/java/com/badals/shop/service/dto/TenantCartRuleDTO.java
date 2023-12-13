package com.badals.shop.service.dto;

import com.badals.shop.domain.enumeration.DiscountReductionType;
import com.badals.shop.domain.enumeration.DiscountType;
import com.badals.shop.domain.pojo.DiscountCheckoutRule;
import com.badals.shop.domain.pojo.DiscountRule;
import com.badals.shop.domain.pojo.I18String;
import com.badals.shop.domain.pojo.PriceMap;
import lombok.Data;

import java.util.List;

@Data
public class TenantCartRuleDTO {
    Long id;
    String coupon;
    Boolean autoApply;
    Boolean canCombine;
    Boolean enabled;
    Integer priority;
    List<I18String> description;
    List<DiscountRule> rules;
    List<DiscountCheckoutRule> checkoutRules;
    DiscountType discountType;
    DiscountReductionType reductionType;
    String discount;
    Long customerId;
    String tenantId;
}
