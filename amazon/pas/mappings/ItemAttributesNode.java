package com.badals.shop.vendor.amazon.pas.mappings;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemAttributesNode {
   
   @XStreamAlias("Binding")
   String binding;
   
   @XStreamAlias("Brand")
   String brand;

   @XStreamImplicit(itemFieldName="Feature")
   public final List<String> features;

   
   //@XStreamAlias("IsAdultProduct")
   @Builder.Default
   String IsAdultProduct = "False";
   
   
   //@XStreamAlias("IsMemorabilia")
   @Builder.Default
   String IsMemorabilia  = "False";
   
   @XStreamAlias("ItemDimensions")
   DimensionsNode itemDimensions;
   
   @XStreamAlias("Label")
   String label;
   
   @XStreamAlias("ListPrice")
   PriceNode listPrice;
   
   @XStreamAlias("Manufacturer")
   String manufacturer;
   
   @XStreamAlias("Model")
   String model;

   @XStreamAlias("MPN")
   String mpn;

   @XStreamAlias("PackageDimensions")
   DimensionsNode packageDimensions;

   @XStreamAlias("PackageQuantity")
   String packageQuantity;

   @XStreamAlias("PartNumber")
   String partNumber;
   
   @XStreamAlias("ProductGroup")
   String productGroup;

   @XStreamAlias("ProductTypeName")
   String ProductTypeName;

   @XStreamAlias("Publisher")
   String publisher;

   @XStreamAlias("Studio")
   String studio;

   @XStreamAlias("Title")
   String title;
   
   @XStreamAlias("ReleaseDate")
   String releaseDate;
   
   @XStreamAlias("UPC")
   String upc;
   
   @XStreamAlias("EAN")
   String ean; 
}
