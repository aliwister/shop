package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.VariationOption;
import com.badals.shop.service.dto.ProductLangDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PartnerProduct extends AddProductDTO {
   public PartnerProduct() {
   }

   public PartnerProduct(Long id, String sku, String name, String name_ar, List<String> shops) {
      this.id = id;
      this.sku = sku;
      this.name = name;
      this.name_ar = name_ar;
      this.gallery = shops;
   }


   List<ChildProduct> children;
   List<Price> prices;
   List<ProductLangDTO> langs;
   List<VariationOption> options;

}
