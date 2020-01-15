package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemLookupResponse {
   //@JsonIgnore 
   //OperationRequest OperationRequest;
   //@JacksonXmlProperty(localName = "Items")
   
   @XStreamAlias("Items")
   private ItemsNode Items;
   
   /*@JacksonXmlElementWrapper(localName = "Items")
   @JacksonXmlProperty(localName = "Item")
   final public List<Item> items = new ArrayList();
   */
   
   
}
