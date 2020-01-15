package com.badals.shop.vendor.amazon.pas.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import lombok.Data;

@Data
public class AncestorsNode {
   @XStreamAlias("BrowseNode")
   public BrowseNodeNode browseNode;
}
