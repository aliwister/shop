package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDefNode {
   @JacksonXmlProperty(localName = "URL")
   String URL;
   
   @JacksonXmlProperty(localName = "Height")
   String Height;

   @JacksonXmlProperty(localName = "Width")
   String Width;
}
