package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class PriceList implements Serializable {
    List<Price> priceList = new ArrayList<>();
    String baseCurrency;

    public String getPriceForCurrency(String targetCurrency) {
        Price p = priceList.stream().filter(x -> x.getCurrency().equals(targetCurrency)).findFirst().orElse(null);
        if (p != null)
            return p.getAmount().toString();
        return null;
    }
}
