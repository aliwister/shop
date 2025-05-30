package com.badals.shop.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.*;

/**
 * Service Implementation for managing {@link Currency}.
 */
@Service
@Transactional
public class CurrencyService {

    public static final String BASE_CURRENCY_KEY = "base";
    @Value("${currency.apikey}")
    private static String API_KEY;
    private static final Map<String, String> expiringMap = new PassiveExpiringMap<>(6 * 60 * 60 * 1000);

    private final Logger log = LoggerFactory.getLogger(CurrencyService.class);

    public static String convert(String amount, String from, String to) {
        if (amount == null) return null;
        String rate = expiringMap.get(from + to);
        if (rate == null) {
            rate = getRate(from, to);
            expiringMap.put(from + to, rate);
        }
        Locale locale = LocaleContextHolder.getLocale();
        Currency currency = Currency.getInstance(locale);
        NumberFormat format = NumberFormat.getNumberInstance();
        format.setMaximumFractionDigits(2);
        double price = Double.parseDouble(amount) * Double.parseDouble(rate);
        return format.format(price);
    }

    @SneakyThrows
    public static String getRate(String from, String to) {
        String url_str = "https://api.apilayer.com/exchangerates_data/convert?from=" + from + "&to=" + to + "&amount=1";

        URL url = new URL(url_str);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setRequestProperty("apikey", API_KEY);
        request.connect();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
        JsonObject jsonobj = root.getAsJsonObject();

        return jsonobj.get("result").getAsString();
    }


}
