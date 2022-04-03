package com.badals.shop.aop.tenant;

import com.badals.shop.config.Constants;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Aspect
@Component
public class LocaleHeaderAspect {

   private final Logger log = LoggerFactory.getLogger(LocaleHeaderAspect.class);

   /**
    * Reads the Accept-Language header and sets the locale to that value for the current request.
    * @throws Throwable
    */

   @Before("execution(* com.badals.shop.service.*.*(..))")
   public void beforeExecution() throws Throwable {
      if (RequestContextHolder.getRequestAttributes() == null) return;

      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
      String localeHeader = request.getHeader("Accept-Language");
      if(localeHeader != null && !localeHeader.equals("undefined")) {
         List<Locale.LanguageRange> list = Locale.LanguageRange.parse(localeHeader);
         Locale locale = Locale.lookup(list, Constants.LOCALES);
         LocaleContextHolder.setLocale(locale);
      }
   }
}