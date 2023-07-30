package com.badals.shop.domain.pojo;

import lombok.Data;

import java.util.List;

@Data
public class DiscountCheckoutRule {
   List<String> excludedCarriers;
   List<String> excludedPayments;
}
