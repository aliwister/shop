package com.badals.shop.xtra.amazon.mappings;

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

    public String getBrowseNodeId() {
        return browseNodeId;
    }

    public void setBrowseNodeId(String browseNodeId) {
        this.browseNodeId = browseNodeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsCategoryRoot() {
        return isCategoryRoot;
    }

    public void setIsCategoryRoot(String isCategoryRoot) {
        this.isCategoryRoot = isCategoryRoot;
    }

    public AncestorsNode getAncestors() {
        return ancestors;
    }

    public void setAncestors(AncestorsNode ancestors) {
        this.ancestors = ancestors;
    }
}
