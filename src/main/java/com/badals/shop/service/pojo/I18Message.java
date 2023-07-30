package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.I18String;
import lombok.Data;

import java.util.List;

@Data
public class I18Message {
    List<I18String> value;
    String status;

    public I18Message(List<I18String>  value) {
        this.value = value;
    }

    public I18Message(List<I18String>  value, String status) {
        this.value = value;
        this.status = status;
    }
}
