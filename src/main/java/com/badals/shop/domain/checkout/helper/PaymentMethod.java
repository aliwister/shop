package com.badals.shop.domain.checkout.helper;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentMethod implements Serializable {
    String ref;

}
