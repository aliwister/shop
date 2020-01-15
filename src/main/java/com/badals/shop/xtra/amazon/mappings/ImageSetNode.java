package com.badals.shop.xtra.amazon.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
public class ImageSetNode {
   @XStreamAlias("Category")
   @XStreamAsAttribute
   String category;

   @XStreamAlias("SwatchImage")
   ImageDefNode swatchImage;

   @XStreamAlias("ThumbnailImage")
   ImageDefNode thumbnailImage;

   @XStreamAlias("TinyImage")
   ImageDefNode tinyImage;

   @XStreamAlias("SmallImage")
   ImageDefNode smallImage;

   @XStreamAlias("MediumImage")
   ImageDefNode mediumImage;

   @XStreamAlias("LargeImage")
   ImageDefNode largeImage;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ImageDefNode getSwatchImage() {
        return swatchImage;
    }

    public void setSwatchImage(ImageDefNode swatchImage) {
        this.swatchImage = swatchImage;
    }

    public ImageDefNode getThumbnailImage() {
        return thumbnailImage;
    }

    public void setThumbnailImage(ImageDefNode thumbnailImage) {
        this.thumbnailImage = thumbnailImage;
    }

    public ImageDefNode getTinyImage() {
        return tinyImage;
    }

    public void setTinyImage(ImageDefNode tinyImage) {
        this.tinyImage = tinyImage;
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
}
