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

   BigDecimal listPrice;
   BigDecimal savingsAmount;
   Integer savingsPercentage;
   String maxHours;
   String title;
   String description;


   String itemWeight;
   String itemWeightUnit;
   String packageWeight;

   String packageHeight;
   String packageLength;
   String packageWidth;

   String itemLengthUnit;

   String itemHeight;
   String itemLength;
   String itemWidth;

   boolean adultProduct;
   String unitCount;

   String starRating;


   /** Attributes **/
   String size;
   String edition;
   String format;
   String type;
   String upc;
   String ean;
   String isbn;
   String productGroup;
   String productType;
   String partNumber;
   String releaseDate;
   String color;
   String model;
   String manufacturer;
   String author;
   String binding;
   Integer numberOfPages;
   Integer publicationDate;
   String audienceRating;
   /*************/

   List<String> frequentlyBoughtTogether;
   List<PasItemNode> children;
   List<String> features;

   List<String> similarities = new ArrayList<>();
   List<String> HierarchialCategories = new ArrayList<>();
   List<String> numHierarchialCategories = new ArrayList<>();
   List<String> categories = new ArrayList<>();
   List<String> numCategories = new ArrayList<>();

   String browseNode;
   String editorial;



   VariationType variationType;

   List<Attribute> variationAttributes = new ArrayList<>();
   HashSet<String> variationDimensions = new HashSet<>();

   HashMap<String, List<Attribute>> variations = new HashMap<String, List<Attribute>>();

   boolean superSaver = false;
   boolean prime = false;
   boolean freeShipping = false;
   boolean overSize = false;
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
   private static final BigDecimal LB2G = BigDecimal.valueOf(453.6);
   private static final BigDecimal LB2OUNCE = BigDecimal.valueOf(14);


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
         else if (getItemWeightUnit().equalsIgnoreCase("ounces"))
            return w.divide(LB2OUNCE, 4, RoundingMode.HALF_EVEN);

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


   public List<Attribute> parseAttributes() {
      List map = new ArrayList();
      if( size != null)
         map.add(new Attribute("size", size));
      if( edition != null)
         map.add(new Attribute("edition", edition));
      if( format != null)
         map.add(new Attribute("format", format));
      if( type != null)
         map.add(new Attribute("type", type));
      if( upc != null)
         map.add(new Attribute("upc", upc));
      if( ean != null)
         map.add(new Attribute("ean", ean));
      if( isbn != null)
         map.add(new Attribute("isbn", isbn));
      if( productGroup != null)
         map.add(new Attribute("productGroup", productGroup));
      if( productType != null)
         map.add(new Attribute("productType", productType));
      if( partNumber != null)
         map.add(new Attribute("partNumber", partNumber));
      if( releaseDate != null)
         map.add(new Attribute("releaseDate", releaseDate));
      if( color != null)
         map.add(new Attribute("color", color));
      if( model != null)
         map.add(new Attribute("model", model));
      if( manufacturer != null)
         map.add(new Attribute("manufacturer", manufacturer));
      if( author != null)
         map.add(new Attribute("author", author));
      if( binding != null)
         map.add(new Attribute("binding", binding));
      if( numberOfPages != null)
         map.add(new Attribute("numberOfPages", String.valueOf(numberOfPages)));
      if( publicationDate != null)
         map.add(new Attribute("publicationDate", String.valueOf(publicationDate)));
      if( audienceRating != null)
         map.add(new Attribute("audienceRating", audienceRating));
      return map;
   }
}
