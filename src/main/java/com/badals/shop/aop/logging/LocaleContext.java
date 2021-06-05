package com.badals.shop.aop.logging;

public class LocaleContext {
   public static String getLocale() {
      return locale.get();
   }

   public static void setLocale(String locale) {
      LocaleContext.locale.set(locale);
   }

   private static ThreadLocal<String> locale = new InheritableThreadLocal<>();

   public static void clear() {
      locale.set(null);
   }
}
