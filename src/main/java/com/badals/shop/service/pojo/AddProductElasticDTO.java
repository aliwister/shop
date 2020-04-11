package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.MerchantStockDTO;
import com.badals.shop.service.dto.ProductLangDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@org.springframework.data.elasticsearch.annotations.Document(indexName = "add-product")
public class AddProductElasticDTO {
   Long id;
   String sku;
   String upc;
   String ref;
   String slug;
   String price;
   String image;
   String name;
   String name_ar;
   String brand;
   String brand_ar;
   String description;
   String description_ar;
   String features;
   String features_ar;
   String cost;
   String weight;
   String type;
   String unit;
   String availability;
   String salePrice;
   String quantity;
   String gallery;
   String discountInPercent;

   String shopIds;
   String browseNode;
   String variationType;

   public AddProductElasticDTO(Long id, String sku, String name, String name_ar) {
      this.id = id;
      this.sku = sku;
      this.name = name;
      this.name_ar = name_ar;
   }

}
