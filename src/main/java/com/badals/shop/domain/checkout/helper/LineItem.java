package com.badals.shop.domain.checkout.helper;

import lombok.Data;

import java.io.Serializable;

@Data
public class LineItem implements Serializable {
    Integer productId;
    String sku;
    String unit;
    String image;
    String name;
    Float price;
    Float quantity;

    Float subTotal;
}
