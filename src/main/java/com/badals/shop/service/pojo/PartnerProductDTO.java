package com.badals.shop.service.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PartnerProductDTO extends AddProductDTO {
   public PartnerProductDTO() {
   }

   public PartnerProductDTO(Long id, String sku, String name, String name_ar, List<String> shops) {
      this.id = id;
      this.sku = sku;
      this.name = name;
      this.name_ar = name_ar;
      this.gallery = shops;
   }
}
