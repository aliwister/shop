package com.badals.shop.vendor.amazon.pas.mappings;

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
}
