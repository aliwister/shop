package com.badals.shop.aop.logging;

public class LocaleContext {
   public static String getLocale() {
      String ret = locale.get();
      if (ret == null || ret.length() < 2)
         return "en-OM";
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
