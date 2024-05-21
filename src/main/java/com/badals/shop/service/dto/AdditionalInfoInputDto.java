package com.badals.shop.service.dto;

import lombok.Data;

import java.util.List;

@Data
public class AdditionalInfoInputDto {
    private String date;
    private String sender_state;
    private Float price;
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
    private List<ItemInput> items;
    private List<PackageLineItemInput> requestedPackageLineItems;
    private String sender_phone;
    private String receiver_phone;
    private String receiver_countryCode;
}
