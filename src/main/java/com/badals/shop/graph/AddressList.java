package com.badals.shop.graph;

import com.badals.shop.service.dto.AddressDTO;
import lombok.Data;

import java.util.List;

@Data
public class AddressList {
   List<AddressDTO> addresses;
   String displayFormat;

   public AddressList(List<AddressDTO> addresses, String displayFormat) {
      this.addresses = addresses;
      this.displayFormat = displayFormat;
   }
}
