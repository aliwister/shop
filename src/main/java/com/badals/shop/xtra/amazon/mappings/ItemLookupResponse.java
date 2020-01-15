package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemLookupResponse {
   //@JsonIgnore
   //OperationRequest OperationRequest;
   //@JacksonXmlProperty(localName = "Items")

    @Id
    private String sku;

   @XStreamAlias("Items")
   private ItemsNode Items;

   /*@JacksonXmlElementWrapper(localName = "Items")
   @JacksonXmlProperty(localName = "Item")
   final public List<Item> items = new ArrayList();
   */

    public ItemsNode getItems() {
        return Items;
    }

    public void setItems(ItemsNode items) {
        Items = items;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
}
