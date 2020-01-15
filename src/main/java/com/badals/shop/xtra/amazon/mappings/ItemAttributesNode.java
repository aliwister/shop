package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemAttributesNode {

   @XStreamAlias("Binding")
   String binding;

   @XStreamAlias("Brand")
   String brand;

   @XStreamImplicit(itemFieldName="Feature")
   public final List<String> features = null;


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


    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public List<String> getFeatures() {
        return features;
    }

    public String getIsAdultProduct() {
        return IsAdultProduct;
    }

    public void setIsAdultProduct(String isAdultProduct) {
        IsAdultProduct = isAdultProduct;
    }

    public String getIsMemorabilia() {
        return IsMemorabilia;
    }

    public void setIsMemorabilia(String isMemorabilia) {
        IsMemorabilia = isMemorabilia;
    }

    public DimensionsNode getItemDimensions() {
        return itemDimensions;
    }

    public void setItemDimensions(DimensionsNode itemDimensions) {
        this.itemDimensions = itemDimensions;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public PriceNode getListPrice() {
        return listPrice;
    }

    public void setListPrice(PriceNode listPrice) {
        this.listPrice = listPrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getMpn() {
        return mpn;
    }

    public void setMpn(String mpn) {
        this.mpn = mpn;
    }

    public DimensionsNode getPackageDimensions() {
        return packageDimensions;
    }

    public void setPackageDimensions(DimensionsNode packageDimensions) {
        this.packageDimensions = packageDimensions;
    }

    public String getPackageQuantity() {
        return packageQuantity;
    }

    public void setPackageQuantity(String packageQuantity) {
        this.packageQuantity = packageQuantity;
    }

    public String getPartNumber() {
        return partNumber;
    }

    public void setPartNumber(String partNumber) {
        this.partNumber = partNumber;
    }

    public String getProductGroup() {
        return productGroup;
    }

    public void setProductGroup(String productGroup) {
        this.productGroup = productGroup;
    }

    public String getProductTypeName() {
        return ProductTypeName;
    }

    public void setProductTypeName(String productTypeName) {
        ProductTypeName = productTypeName;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getEan() {
        return ean;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }
}
