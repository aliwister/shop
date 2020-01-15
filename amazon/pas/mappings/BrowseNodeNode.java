package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BrowseNodeNode {
   @XStreamAlias("BrowseNodeId")
   String browseNodeId;
   
   @XStreamAlias("Name")
   String name;
   
   @XStreamAlias("IsCategoryRoot")
   String isCategoryRoot;
   
   @XStreamAlias("Ancestors")
   AncestorsNode ancestors;
}
