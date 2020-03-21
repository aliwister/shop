package com.badals.shop.service.util;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.function.Supplier;

/**
 * Utility class for generating random Strings.
 */
public final class AccessUtil {
    private AccessUtil() {
    }

    public static <T> T opt(Supplier<T> statement) {
        try {
            return statement.get();
        } catch (NullPointerException exc) {
            return null;
        }
    }

}
