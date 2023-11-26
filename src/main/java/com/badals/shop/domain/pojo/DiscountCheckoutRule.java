package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DiscountCheckoutRule implements Serializable {
    List<String> excludedCarriers;
    List<String> excludedPayments;
}
