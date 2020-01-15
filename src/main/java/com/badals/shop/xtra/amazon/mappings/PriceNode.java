package com.badals.shop.xtra.amazon.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class PriceNode {
   @XStreamAlias("Amount")
   String amount;
   @XStreamAlias("CurrencyCode")
   String currencyCode;

   @XStreamAlias("FormattedPrice")
   String formattedPrice;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getFormattedPrice() {
        return formattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        this.formattedPrice = formattedPrice;
    }
}
