package com.badals.shop.domain.checkout.helper;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressPojo implements Serializable {
    String name;
    String line1;
    String line2;
    String city;
    String country;
    String postalCode;
}
