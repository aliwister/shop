package com.badals.shop.service.pojo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@org.springframework.data.elasticsearch.annotations.Document(indexName = "add-product")
public class AddProductDTO {
   public AddProductDTO() {
   }

   public AddProductDTO(Long id, String sku, String name, String name_ar, List<String> shops) {
      this.id = id;
      this.sku = sku;
      this.name = name;
      this.name_ar = name_ar;
      this.gallery = shops;
   }

   Long id;
   String sku;
   String upc;
   Long ref;
   String slug;
   BigDecimal price;
   String image;
   String name;
   String name_ar;
   String brand;
   String brand_ar;
   String description;
   String description_ar;
   String features;
   String features_ar;
   BigDecimal cost;
   BigDecimal weight;
   String type;
   String unit;
   Integer availability;
   BigDecimal salePrice;
   BigDecimal quantity;

   Boolean active;

   List<String> gallery;

   Integer discountInPercent;

   List<String> shops;

   List<Long> shopIds;

   String browseNode;
   String browseNode_ar;
   String variationType;
   Boolean imported;
   Boolean indexed;
   String tenant;
   String merchant;

   Long merchantId;

   String url;


   Boolean inStock;
   List<String> hashtags;
}
