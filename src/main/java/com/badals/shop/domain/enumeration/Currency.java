package com.badals.shop.domain.enumeration;

import java.io.Serializable;

/**
 * The State enumeration.
 */

public enum Currency implements Serializable {
    OMR("OMR"),
    AED("AED"),
    SAR("SAR"),
    KWD("KWD"),
    BHD("BHD"),
    QAR("QAR"),
    USD("USD"),
    EUR("EUR");

    private String code;

    Currency(String code) {
        this.code = code;
    }
}
