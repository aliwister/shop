package com.badals.shop.web.rest.errors;

public class InvalidPhoneException extends Exception {
   public InvalidPhoneException(String mobile_not_provided) {
      super(mobile_not_provided);
   }
}
