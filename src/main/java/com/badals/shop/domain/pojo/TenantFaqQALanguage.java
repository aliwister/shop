package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TenantFaqQALanguage implements Serializable {

    private Long id;
    private String question;
    private String answer;
    private String language;
    private String tenantId;

}
