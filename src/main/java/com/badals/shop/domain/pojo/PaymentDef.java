package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PaymentDef implements Serializable {
   String name;
   String pk;
   String sk;
   String code;
}
