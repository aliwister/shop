package com.badals.shop.service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SignupChoiceDTO implements Serializable {

    private String responseCode;
    private String response;
}
