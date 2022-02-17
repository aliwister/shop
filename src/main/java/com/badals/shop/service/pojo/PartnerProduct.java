package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.*;
import lombok.Data;

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
   public PriceList listPriceObj = new PriceList();
   public PriceList priceObj = new PriceList();

   public Price costObj= new Price();

   String model;

   List<TenantProductLang> langs;
   List<VariationOption> options;

}
