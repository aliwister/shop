package com.badals.shop.service.pojo;

import lombok.Data;

@Data
public class Message {
    String value;
    String status;

    public Message(String value) {
        this.value = value;
    }

    public Message(String value, String status) {
        this.value = value;
        this.status = status;
    }
}
