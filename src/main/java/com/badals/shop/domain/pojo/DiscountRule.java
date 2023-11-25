package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class DiscountRule implements Serializable {
    Integer minCartSize;
}
