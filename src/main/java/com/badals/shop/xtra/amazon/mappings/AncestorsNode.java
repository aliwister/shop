package com.badals.shop.xtra.amazon.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
public class AncestorsNode {
   @XStreamAlias("BrowseNode")
   public BrowseNodeNode browseNode;

    public BrowseNodeNode getBrowseNode() {
        return browseNode;
    }

    public void setBrowseNode(BrowseNodeNode browseNode) {
        this.browseNode = browseNode;
    }
}
