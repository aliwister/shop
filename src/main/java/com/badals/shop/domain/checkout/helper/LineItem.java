package com.badals.shop.domain.checkout.helper;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
//@Accessors(fluent = true)
public class LineItem implements Serializable {
    Integer productId;
    String sku;
    String unit;
    String ref;
    String image;
    String name;
    BigDecimal price;
    BigDecimal cost;
    Float quantity;
    BigDecimal weight;
    Double subTotal;
    String url;
}
