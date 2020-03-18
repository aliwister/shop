package com.badals.shop.web.rest.errors;

public class OrderNotFoundException extends Exception {
   public OrderNotFoundException(String message) {
      super(message);
   }
}
