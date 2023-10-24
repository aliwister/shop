package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;


@Data
public class TenantFaqCategoryName implements Serializable {

    private String language;
    private String name;

}

