package com.badals.shop.xtra.amazon.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class VariationsNode {
   @XStreamAlias("TotalVariations")
   String totalVariations;

   @XStreamAlias("TotalVariationPages")
   String totalVariationPages;

   @XStreamAlias("VariationDimensions")
   public final List<String> variationDimensions = new ArrayList<>();

   @XStreamAlias("Item")
   public List<ItemNode> items = new LinkedList<>();

    public String getTotalVariations() {
        return totalVariations;
    }

    public void setTotalVariations(String totalVariations) {
        this.totalVariations = totalVariations;
    }

    public String getTotalVariationPages() {
        return totalVariationPages;
    }

    public void setTotalVariationPages(String totalVariationPages) {
        this.totalVariationPages = totalVariationPages;
    }

    public List<String> getVariationDimensions() {
        return variationDimensions;
    }

    public List<ItemNode> getItems() {
        return items;
    }

    public void setItems(List<ItemNode> items) {
        this.items = items;
    }
}
