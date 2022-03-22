package com.badals.shop.config;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.TranslateOptions;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


import java.io.IOException;

@Configuration
public class GoogleTranslateConfiguration {
    @Bean
    Translate getTranslate() {
        return TranslateOptions.getDefaultInstance().getService();
    }
}
