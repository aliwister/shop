package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@org.springframework.data.elasticsearch.annotations.Document(indexName = "product")
public class PartnerProduct {
   Long id;
   String sku;
   String upc;
   Long ref;
   String slug;
   String image;
   String name;
   BigDecimal weight;
   String type;
   String unit;
   Integer availability;
   BigDecimal quantity;

   Boolean active;

   List<String> gallery;

   List<String> shops;

   List<Long> shopIds;

   String variationType;
   Boolean imported;
   Boolean indexed;
   Boolean isPos;
   String tenantId;
   String merchant;

   Long merchantId;

   String url;


   Boolean inStock;
   List<String> hashtags;
   String title;


   List<ChildProduct> children;
   public PriceList listPrice = new PriceList();
   public PriceList price = new PriceList();

   public Price cost= new Price();

   String model;
   String brand;

   List<TenantProductLang> langs;
   List<AttributesLang> attributes;
   List<VariationOption> options;

   List<Attribute> deliveryProfiles;

}
