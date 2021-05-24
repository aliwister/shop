package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;
import lombok.Data;

import java.util.List;

@Data
public class ChildProduct extends AddProductDTO {
   public ChildProduct() {
   }

   public ChildProduct(Long id, String sku, String name, String name_ar, List<String> shops) {
      this.id = id;
      this.sku = sku;
      this.name = name;
      this.name_ar = name_ar;
      this.gallery = shops;
   }

   List<Attribute> variationAttributes;
   public Price priceObj = new Price();
   public Price salePriceObj = new Price();
   public Price costObj= new Price();

   public Boolean isDirty;
   public Boolean active;
}
