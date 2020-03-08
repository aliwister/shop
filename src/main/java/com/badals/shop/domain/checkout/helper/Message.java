package com.badals.shop.domain.checkout.helper;

import lombok.Data;

@Data
public class Message {
    String value;

    public Message(String value) {
        this.value = value;
    }
}
