package com.badals.shop.vendor.amazon.pas.mappings;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemNode {
   @XStreamAlias("ASIN")
   String asin;
   
   @XStreamAlias("ParentASIN")
   String parentAsin;
   
   @XStreamAlias("DetailPageURL")
   String detailPageURL;
   
   //@XStreamAlias("ItemLink")
   //public final List<ItemLinkNode> itemLinks = new ArrayList<ItemLink>();
   
   //@JsonIgnore
   @XStreamAlias("SmallImage")
   ImageDefNode smallImage;
   
   //@JsonIgnore
   @XStreamAlias("MediumImage")
   ImageDefNode mediumImage;
   
   //@JsonIgnore
   @XStreamAlias("LargeImage")
   ImageDefNode largeImage;
   
   @XStreamAlias("ImageSets")
   public final List<ImageSetNode> imageSets = new ArrayList<>();
   
   @XStreamAlias("ItemAttributes")
   public ItemAttributesNode ItemAttributes;
   
   @XStreamAlias("Offers")
   public OffersNode offers;
   
   @XStreamAlias("CustomerReviews")
   CustomerReviewsNode customerReviews;
   
   @XStreamAlias("EditorialReviews")
   public final List<EditorialReviewNode> editorialReviews = new ArrayList<>();
   
   @XStreamAlias("SimilarProducts")
   public final List<SimilarProductNode> similarProducts = new ArrayList<>();
   
   @XStreamAlias("BrowseNodes")
   public BrowseNodesNode browseNodes;
   
   @XStreamAlias("Variations")
   VariationsNode variations;
   
   @XStreamAlias("VariationAttributes")
   public final List<VariationAttributeNode> variationAttributes = new ArrayList<>();
}
