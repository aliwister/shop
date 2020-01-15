package com.badals.shop.vendor.amazon.pas.mappings;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemLookupRequestNode {
   
   @JacksonXmlProperty(localName = "IdType")
   @XStreamAlias("IdType")
   String IdType;
  /* @JacksonXmlProperty(localName = "ItemId")
   @XStreamAlias("ItemId")
   String ItemId;*/
   @JacksonXmlProperty(localName = "ResponseGroup")

   List<String> ResponseGroups;
   @JacksonXmlProperty(localName = "VariationPage")
   @XStreamAlias("VariationPage")
   String VariationPage;
}
