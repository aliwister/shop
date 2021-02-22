package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.service.dto.ProductLangDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductEnvelope {
   public ProductEnvelope() {
   }
   String message;
   PartnerProduct product;
   Integer code;

   public ProductEnvelope(PartnerProduct product, String message, Integer code) {
      this.message = message;
      this.product = product;
      this.code = code;
   }
}
