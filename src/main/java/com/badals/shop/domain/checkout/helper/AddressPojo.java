package com.badals.shop.domain.checkout.helper;

import lombok.Data;

import java.io.Serializable;

@Data
public class AddressPojo implements Serializable {
    Long id;
    String firstName;
    String lastName;
    String line1;
    String line2;
    String city;
    String state;
    String country;
    String postalCode;
    String mobile;
}
