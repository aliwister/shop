package com.badals.shop.graph;

import com.badals.shop.service.dto.AddressDTO;
import lombok.Data;

@Data
public class AddressResponse extends MutationResponse {
   private AddressDTO address;
   private String code;
   private Boolean success;
   private String message;

   public AddressResponse() {
   }

   public AddressResponse(AddressDTO address, String code, Boolean success, String message) {
      this.address = address;
      this.code = code;
      this.success = success;
      this.message = message;
   }
}
