package com.badals.shop.domain.pojo;

import java.util.List;

public class Variation {
    long ref;
    List<Attribute> variationAttributes;

    public Variation() {
    }

    public Variation(long ref, List<Attribute> variationAttributes) {
        this.ref = ref;
        this.variationAttributes = variationAttributes;
    }

    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    public List<Attribute> getVariationAttributes() {
        return variationAttributes;
    }

    public void setVariationAttributes(List<Attribute> variationAttributes) {
        this.variationAttributes = variationAttributes;
    }
}
