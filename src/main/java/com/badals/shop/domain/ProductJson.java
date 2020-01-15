package com.badals.shop.domain;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProductJson {
   @Id
   Long id;
   Long parent;
   String rewrite;
   String upc;
   String title;
   String description;
   String brand;
   String model;
   List<String> features;
   Map<String, Double> price;
   String group;
   String image;
   List<String> imageSets;
   Date releaseDate;
   Map<String, String> variationAttributes;
   Map<String, Set<String>> variationDimensions;
   Map<Long, Map<String, String>> variations;
   Boolean active;

   List<Integer> similarProducts;
   String externalUrl;
   //End

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public String getRewrite() {
        return rewrite;
    }

    public void setRewrite(String rewrite) {
        this.rewrite = rewrite;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public Map<String, Double> getPrice() {
        return price;
    }

    public void setPrice(Map<String, Double> price) {
        this.price = price;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImageSets() {
        return imageSets;
    }

    public void setImageSets(List<String> imageSets) {
        this.imageSets = imageSets;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Map<String, String> getVariationAttributes() {
        return variationAttributes;
    }

    public void setVariationAttributes(Map<String, String> variationAttributes) {
        this.variationAttributes = variationAttributes;
    }

    public Map<String, Set<String>> getVariationDimensions() {
        return variationDimensions;
    }

    public void setVariationDimensions(Map<String, Set<String>> variationDimensions) {
        this.variationDimensions = variationDimensions;
    }

    public Map<Long, Map<String, String>> getVariations() {
        return variations;
    }

    public void setVariations(Map<Long, Map<String, String>> variations) {
        this.variations = variations;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Integer> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<Integer> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getExternalUrl() {
        return externalUrl;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

/*
   String type;
   String store;
   boolean active;

   //@DiffIgnore
   Date updated;
   String location;

   String price;
   boolean display_list_price;
   boolean top_level;
   //Map current_variation
   //badge
   Integer store_id;
   boolean in_stock;
   String category;
   //List labels //dispatches from us



   // Item attributes
   String asin;
   String parentAsin;
   String link;

   //ItemAttributes
   String binding;

   String ean;
   List<String> features;

   boolean adultProduct;
   boolean memorabilia;

   //@DiffIgnore
   Dimensions itemDimensions;

   String label;
   Price listPrice;
   String manufacturer;
   String model;
   Dimensions packageDimensions;
   String partNumber;
   String productGroup;
   String productType;
   String publisher;
   String releaseDate;
   String studio;
   String upc;


   //Description
   String description;

   //Variations
   List<String> variationDimensions;

   Map<String, Set<String>> variationOptions;
   Map<Long, Map<String, String>> variations;
   Map<String, String> variationAttributes;

   public void addVariation(Long id, Map<String, String> map) {
      //if(map != null && variations != null)
      variations.put(id, map);
      map.forEach((k, v) -> variationOptions.get(k).add(v));
   }

   //@DiffIgnore
   List<String> image_sets;

   //@DiffIgnore
   List<BrowseNode> vendor_crumbs;

   //@DiffIgnore
   List<String> similarProducts;

   //@DiffIgnore
   ProductBody parentObj;


   String className;*/
}
