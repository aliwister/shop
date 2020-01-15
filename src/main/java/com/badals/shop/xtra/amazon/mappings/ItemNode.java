package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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

    public String getAsin() {
        return asin;
    }

    public void setAsin(String asin) {
        this.asin = asin;
    }

    public String getParentAsin() {
        return parentAsin;
    }

    public void setParentAsin(String parentAsin) {
        this.parentAsin = parentAsin;
    }

    public String getDetailPageURL() {
        return detailPageURL;
    }

    public void setDetailPageURL(String detailPageURL) {
        this.detailPageURL = detailPageURL;
    }

    public ImageDefNode getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(ImageDefNode smallImage) {
        this.smallImage = smallImage;
    }

    public ImageDefNode getMediumImage() {
        return mediumImage;
    }

    public void setMediumImage(ImageDefNode mediumImage) {
        this.mediumImage = mediumImage;
    }

    public ImageDefNode getLargeImage() {
        return largeImage;
    }

    public void setLargeImage(ImageDefNode largeImage) {
        this.largeImage = largeImage;
    }

    public List<ImageSetNode> getImageSets() {
        return imageSets;
    }

    public ItemAttributesNode getItemAttributes() {
        return ItemAttributes;
    }

    public void setItemAttributes(ItemAttributesNode itemAttributes) {
        ItemAttributes = itemAttributes;
    }

    public OffersNode getOffers() {
        return offers;
    }

    public void setOffers(OffersNode offers) {
        this.offers = offers;
    }

    public CustomerReviewsNode getCustomerReviews() {
        return customerReviews;
    }

    public void setCustomerReviews(CustomerReviewsNode customerReviews) {
        this.customerReviews = customerReviews;
    }

    public List<EditorialReviewNode> getEditorialReviews() {
        return editorialReviews;
    }

    public List<SimilarProductNode> getSimilarProducts() {
        return similarProducts;
    }

    public BrowseNodesNode getBrowseNodes() {
        return browseNodes;
    }

    public void setBrowseNodes(BrowseNodesNode browseNodes) {
        this.browseNodes = browseNodes;
    }

    public VariationsNode getVariations() {
        return variations;
    }

    public void setVariations(VariationsNode variations) {
        this.variations = variations;
    }

    public List<VariationAttributeNode> getVariationAttributes() {
        return variationAttributes;
    }
}
