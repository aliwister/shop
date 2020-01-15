package com.badals.shop.xtra.amazon.mappings;

import lombok.Data;

@Data
public class VariationAttributeNode {
   String Name;
   String Value;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValue() {
        return Value;
    }

    public void setValue(String value) {
        Value = value;
    }
}
