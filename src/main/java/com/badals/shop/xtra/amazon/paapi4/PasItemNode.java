package com.badals.shop.xtra.amazon.paapi4;

import com.badals.shop.domain.pojo.Attribute;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PasItemNode {
   String asin;
   String parentAsin;
   String brand;
   String ean;
   String manufacturer;
   String price;
   String maxHours;
   String title;
   String description;

   String model;

   String itemWeight;
   String packageWeight;

   String productGroup;
   String productType;
   String partNumber;


   List<PasItemNode> children;
   List<String> features;

   List<String> similarities = new ArrayList<>();

   String editorial;

   String upc;

   List<Attribute> variationAttributes = new ArrayList<>();

   boolean superSaver = false;
   boolean prime = false;

   String cost;
   String listPrice;
   String merchant;
   String url;

   List<String> gallery = new ArrayList<>();
   String image;

   public PasItemNode(boolean isParent) {
      if(isParent)
         children = new ArrayList<PasItemNode>();
   }

   @Override
   public String toString() {
      return "PasItemNode{" +
              "asin='" + asin + '\'' +
              ", brand='" + brand + '\'' +
              ", ean='" + ean + '\'' +
              ", manufacturer='" + manufacturer + '\'' +
              ", price='" + price + '\'' +
              ", maxHours='" + maxHours + '\'' +
              ", title='" + title + '\'' +
              ", description='" + description + '\'' +
              ", itemWeight='" + itemWeight + '\'' +
              ", packageWeight='" + packageWeight + '\'' +
              ", productGroup='" + productGroup + '\'' +
              ", productType='" + productType + '\'' +
              ", partNumber='" + partNumber + '\'' +
              ", children=" + children +
              ", features=" + features +
              ", upc='" + upc + '\'' +
              ", variationAttributes=" + variationAttributes +
              ", isSuperSaver='" + superSaver + '\'' +
              ", isPrime='" + prime + '\'' +
              ", cost='" + cost + '\'' +
              ", listPrice='" + listPrice + '\'' +
              ", merchant='" + merchant + '\'' +
              ", url='" + url + '\'' +
              ", gallery=" + gallery +
              ", image='" + image + '\'' +
              '}';
   }
}
