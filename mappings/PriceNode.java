package com.badals.shop.vendor.amazon.pas.mappings;

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
}
