package com.badals.shop.domain.enumeration;

import com.badals.shop.domain.pojo.Variation;

import java.util.ArrayList;
import java.util.List;

/**
 * The ProductType enumeration.
 */
public enum VariationType {
    SIMPLE, PARENT, CHILD;
    public static List<VariationType> listTypes = new ArrayList<>() {
        {
        add(SIMPLE);
        add(PARENT);
        }
    };

    public static List<VariationType> getListTypes() {
        return listTypes;
    }
}
