package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityAttributesNode {
   @JacksonXmlProperty(localName = "AvailabilityType")
   String AvailabilityType;
   
   @JacksonXmlProperty(localName = "MinimumHours")
   String MinimumHours;
   
   @JacksonXmlProperty(localName = "MaximumHours")
   String MaximumHours;
}
