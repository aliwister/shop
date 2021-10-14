package com.badals.shop.config;

import io.github.jhipster.config.locale.AngularCookieLocaleResolver;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
//@Configuration
public class LocaleConfiguration implements WebMvcConfigurer {

    @Bean(name = "localeResolver")
    public LocaleResolver localeResolver() {
        return new AcceptHeaderLocaleResolver() {
            @Override
            public Locale resolveLocale (HttpServletRequest request){
                System.out.println("Accept-Language: "+request.getHeader("Accept-Language"));
                if (StringUtils.isBlank(request.getHeader("Accept-Language"))) {
                    return Locale.getDefault();
                }
                List<Locale.LanguageRange> list = Locale.LanguageRange.parse(request.getHeader("Accept-Language"));
                Locale locale = Locale.lookup(list, Constants.LOCALES);
                return locale;
            }
        };
        //cookieLocaleResolver.setCookieName("NG_TRANSLATE_LANG_KEY");
        //return cookieLocaleResolver;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("lang");
        registry.addInterceptor(localeChangeInterceptor);
    }
}