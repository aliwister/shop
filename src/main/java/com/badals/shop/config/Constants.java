package com.badals.shop.config;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Application constants.
 */
public final class Constants {

    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_.@A-Za-z0-9-]*$";
    
    public static final String SYSTEM_ACCOUNT = "system";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static List<Locale> LOCALES = Arrays.asList(
            new Locale("en"),
            new Locale("en_OM"),
            new Locale("en_AE"),
            new Locale("en_QA"),
            new Locale("en_SA"),
            new Locale("en_KW"),
            new Locale("en_BH"),
            new Locale("ar_OM"),
            new Locale("ar_AE"),
            new Locale("ar_QA"),
            new Locale("ar_SA"),
            new Locale("ar_KW"),
            new Locale("ar_BH"),
            new Locale("ar")

    );

    private Constants() {
    }
}
