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

            new Locale("en","om"),
            new Locale("ar","om"),
            new Locale("en","ae"),
            new Locale("ar","ae"),
            new Locale("en","qa"),
            new Locale("ar","qa"),
            new Locale("en","sa"),
            new Locale("ar","sa"),
            new Locale("en","kw"),
            new Locale("ar","kw"),
            new Locale("en","bh"),
            new Locale("ar","bh")
    );

    private Constants() {
    }
}
