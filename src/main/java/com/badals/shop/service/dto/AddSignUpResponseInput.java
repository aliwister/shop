package com.badals.shop.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddSignUpResponseInput implements Serializable {

    private String questionCode;
    private String question;
    private List<SignupChoiceDTO> choices;
    private List<String> responseCodes;
}
