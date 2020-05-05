package com.badals.shop.xtra.amazon;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
class PasBrowseNode {
   public PasBrowseNode(String id, String displayName) {
      this.id = id;
      this.displayName = displayName;
   }

   String id;
   String displayName;
}

@Data
@org.springframework.data.elasticsearch.annotations.Document(indexName = "pas-item")
public class PasItemNode implements Serializable {

   String id;
   String parentAsin;
   String url;
   String brand;

   String manufacturer;
   BigDecimal listPrice;
   BigDecimal savingsAmount;
   Integer savingsPercentage;
   String maxHours;
   String title;
   String description;

   String model;

   String itemWeight;
   String itemWeightUnit;
   String packageWeight;

   String packageHeight;
   String packageLength;
   String packageWidth;

   String itemHeight;
   String itemLength;
   String itemWidth;


   String productGroup;
   String productType;
   String partNumber;
   String releaseDate;
   String color;
   boolean adult;
   String unitCount;
   String size;

   String starRating;

   List<PasItemNode> children;
   List<String> features;

   List<String> similarities = new ArrayList<>();
   List<String> HierarchialCategories = new ArrayList<>();
   List<String> numHierarchialCategories = new ArrayList<>();
   List<String> categories = new ArrayList<>();
   List<String> numCategories = new ArrayList<>();

   String browseNode;
   String editorial;

   String upc;
   String ean;
   String isbn;

   VariationType variationType;

   List<Attribute> variationAttributes = new ArrayList<>();
   HashSet<String> variationDimensions = new HashSet<>();

   HashMap<String, List<Attribute>> variations = new HashMap<String, List<Attribute>>();

   boolean superSaver = false;
   boolean prime = false;
   boolean freeShipping = false;
   BigDecimal shippingCharges;

   BigDecimal omanShippingCharges;

   String availabilityType;
   String availabilityMessage;
   String merchantName;


   String shippingCountry;

   BigDecimal cost;
   String merchant;


   List<String> gallery = new ArrayList<>();
   String image;

   public PasItemNode() {
   }

   public PasItemNode(boolean isParent) {
      if(isParent)
         children = new ArrayList<PasItemNode>();
   }
   private static final BigDecimal LB2KG = BigDecimal.valueOf(0.453592);
   private static final BigDecimal LB2G = BigDecimal.valueOf(200);


   public BigDecimal getParsedWeight() {
      String weight = (this.packageWeight== null)?this.getItemWeight():this.getPackageWeight();

      if(weight != null) {
         BigDecimal w = new BigDecimal(weight);
         if(getItemWeightUnit() == null)
            return w;
         if(getItemWeightUnit().equalsIgnoreCase("kilograms"))
            return w.divide(LB2KG,  4, RoundingMode.HALF_EVEN);
         else if (getItemWeightUnit().equalsIgnoreCase("Grams"))
            return w.divide(LB2G, 4, RoundingMode.HALF_EVEN);

         return w;
         //return BigDecimal.valueOf(Double.parseDouble(weight));
      }
      return null;
   }

   public List<Gallery> gallerizeImages() {
      if(this.gallery == null)
         return null;

      List<Gallery> images = new ArrayList<>();
      for(String gUrl : this.gallery) {
         images.add(new Gallery(gUrl));
      }
      return images;
   }



   @Override
   public String toString() {
      return "PasItemNode{" +
             // "asin='" + asin + '\'' +
              ", brand='" + brand + '\'' +
              ", ean='" + ean + '\'' +
              ", manufacturer='" + manufacturer + '\'' +
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
