package com.badals.shop.vendor.amazon.pas.mappings;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class VariationsNode {
   @XStreamAlias("TotalVariations")
   String totalVariations;
   
   @XStreamAlias("TotalVariationPages")
   String totalVariationPages;
   
   @XStreamAlias("VariationDimensions")
   public final List<String> variationDimensions = new ArrayList<>();
   
   @XStreamAlias("Item")
   public List<ItemNode> items = new LinkedList<>();
}
