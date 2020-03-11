package com.badals.shop.web.rest.errors;

public class ProductNotFoundException extends Exception {
   public ProductNotFoundException(String error) {
      super(error);
   }
}
