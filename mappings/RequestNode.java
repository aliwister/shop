package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestNode {
   //@JacksonXmlProperty(localName = "IsValid")
   @XStreamAlias("IsValid")
   String IsValid;
   
   @JacksonXmlProperty(localName = "ItemLookupRequest")
   ItemLookupRequestNode ItemLookupRequest;
}
