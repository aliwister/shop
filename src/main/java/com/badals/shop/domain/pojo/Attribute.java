package com.badals.shop.domain.pojo;

import java.io.Serializable;

public class Attribute implements Serializable {
    String name;
    String value;

    public Attribute() {
    }

    public Attribute(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
