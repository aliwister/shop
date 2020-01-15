package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemLinkNode {
   @JacksonXmlProperty(localName = "Description")
   String Description;

   @JacksonXmlProperty(localName = "URL")
   String URL;
}
