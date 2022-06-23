package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
public class PriceMap implements Serializable {
   private Map<String, String> prices = new HashMap<>();
   private String base;

   public String getPriceForCurrency(String code) {
      return prices.get(code);
   }

   public void push(String currency, BigDecimal amount) {
      prices.put(currency, amount.toPlainString());
   }

   public PriceMap(String base) {
      this.base = base;
   }

   public PriceMap() {
   }
}
