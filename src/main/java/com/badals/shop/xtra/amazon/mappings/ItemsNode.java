package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemsNode {
   @JacksonXmlProperty(localName = "Request")
   public RequestNode Request;

   //@JacksonXmlElementWrapper(useWrapping = false)
   //@JacksonXmlProperty(localName = "Item")
   //public List<Item> items;
   //Item item;

   @XStreamImplicit
   List<ItemNode> items = new ArrayList<>();

    public RequestNode getRequest() {
        return Request;
    }

    public void setRequest(RequestNode request) {
        Request = request;
    }

    public List<ItemNode> getItems() {
        return items;
    }

    public void setItems(List<ItemNode> items) {
        this.items = items;
    }
}
