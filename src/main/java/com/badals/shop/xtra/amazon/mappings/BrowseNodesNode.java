package com.badals.shop.xtra.amazon.mappings;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

@Data
public class BrowseNodesNode {

    public List<BrowseNodeNode> getBrowseNode() {
        return browseNode;
    }

    @XStreamAlias("BrowseNode")
   @XStreamImplicit
   public final List<BrowseNodeNode> browseNode = null;
}
