package com.badals.shop.graph;

import com.badals.shop.service.dto.AddressDTO;
import lombok.Data;

@Data
public class AddressResponse extends MutationResponse {
   private AddressDTO address;
   private String code;
   private Boolean success;
   private String message;
}
