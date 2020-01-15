package com.badals.shop.vendor.amazon.pas.mappings;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsNode {
   @JacksonXmlProperty(localName = "Request")
   public RequestNode Request;
   
   //@JacksonXmlElementWrapper(useWrapping = false)
   //@JacksonXmlProperty(localName = "Item")
   //public List<Item> items;
   //Item item;
   
   @XStreamImplicit
   List<ItemNode> items = new ArrayList<>();
}
