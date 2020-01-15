package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DimensionsNode {
   @XStreamAlias("Units")
   @XStreamAsAttribute
   String units;
   
   @XStreamAlias("Height")
   String height;
   
   @XStreamAlias("Length")
   String length ;
   
   @XStreamAlias("Weight")
   String weight ;
   
   @XStreamAlias("Width")
   String width;
}
