package com.badals.shop.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdditionalInfoDto {
    private String date;
    private Float price;
    private String sender_state;
    private String zipcode;
    private String sender_zipcode;
    private String email;
    private String name;
    private String company;
    private String city;
    private String state;
    private String address;
    private String address_opt;
    private String sender_name;
    private String sender_company;
    private String sender_address;
    private String sender_address_opt;
    private String sender_city;
    private List<Item> items;
    private List<PackageLineItem> requestedPackageLineItems;
    private String sender_phone;
    private String receiver_phone;
    private String receiver_countryCode;
}

@Data
class Item {
    private String name;
    private String description;
    private String harmonizedCode;
    private String countryOfManufacture;
    private int quantity;
    private String quantityUnits;
    private Weight weight;
    private CustomsValue customsValue;
    private String partNumber;
    private int numberOfPieces;
    private UnitPrice unitPrice;  // Assuming unitPrice is a String
}

@Data
class UnitPrice {
    private float amount;
    private String currency;
}

@Data
class Weight {
    private float value;
    private String units;
}

@Data
class CustomsValue {
    private float amount;
    private String currency;
}

@Data
class PackageLineItem {
    private Weight weight;
    private Dimensions dimensions;
}

@Data
class Dimensions {
    private float length;
    private float width;
    private float height;
    private String units;
}
