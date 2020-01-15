package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestNode {
   //@JacksonXmlProperty(localName = "IsValid")
   @XStreamAlias("IsValid")
   String IsValid;

   @JacksonXmlProperty(localName = "ItemLookupRequest")
   ItemLookupRequestNode ItemLookupRequest;

    public String getIsValid() {
        return IsValid;
    }

    public void setIsValid(String isValid) {
        IsValid = isValid;
    }

    public ItemLookupRequestNode getItemLookupRequest() {
        return ItemLookupRequest;
    }

    public void setItemLookupRequest(ItemLookupRequestNode itemLookupRequest) {
        ItemLookupRequest = itemLookupRequest;
    }
}
