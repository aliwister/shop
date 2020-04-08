package com.badals.shop.service.pojo;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.MerchantService;
import com.badals.shop.service.dto.MerchantStockDTO;
import com.badals.shop.service.dto.ProductLangDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class AddProductDTO {
   Long id;
   String sku;
   String upc;
   String ref;
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
   List<Gallery> gallery;
   Integer discountInPercent;
   List<MerchantStockDTO> merchantStock;
   List<ProductLangDTO> productLangs;
   List<Long> shopIds;
   String browseNode;
}
