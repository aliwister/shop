package com.badals.shop.vendor.amazon.pas.mappings;

import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import lombok.Data;

@Data
public class BrowseNodesNode {

   @XStreamAlias("BrowseNode")
   @XStreamImplicit
   public final List<BrowseNodeNode> browseNode;
}
