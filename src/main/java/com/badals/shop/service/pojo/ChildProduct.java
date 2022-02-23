package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.PriceList;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ChildProduct {
   Long id;
   String sku;
   String upc;
   Long ref;
   String slug;
   String image;
   String name;
   BigDecimal weight;
   List<Attribute> variationAttributes;
   public PriceList price = new PriceList();
   public PriceList listPrice = new PriceList();
   public Price cost = new Price();
   List<String> gallery;

   public Boolean isDirty;
   public Boolean active;
   Integer availability;
   BigDecimal quantity;

   String title;

}
