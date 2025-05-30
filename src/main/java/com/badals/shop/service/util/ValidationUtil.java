package com.badals.shop.service.util;

import java.net.URL;

public class ValidationUtil {
   public static boolean isValidURL(String url)
   {
      /* Try creating a valid URL */
      try {
         new URL(url).toURI();
         return true;
      }

      // If there was an Exception
      // while creating URL object
      catch (Exception e) {
         return false;
      }
   }
}
