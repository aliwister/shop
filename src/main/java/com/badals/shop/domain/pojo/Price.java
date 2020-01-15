package com.badals.shop.domain.pojo;

import java.math.BigDecimal;

public class Price {
    BigDecimal amount;
    String currency;


    public Price() {
    }

    public Price(BigDecimal amount, String currency) {
        this.amount = amount;
        this.currency = currency;
    }

    public String getCurrency() {
        return currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }
}
