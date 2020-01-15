package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ItemLookupRequestNode {

   @JacksonXmlProperty(localName = "IdType")
   @XStreamAlias("IdType")
   String IdType;
  /* @JacksonXmlProperty(localName = "ItemId")
   @XStreamAlias("ItemId")
   String ItemId;*/
   @JacksonXmlProperty(localName = "ResponseGroup")

   List<String> ResponseGroups;
   @JacksonXmlProperty(localName = "VariationPage")
   @XStreamAlias("VariationPage")
   String VariationPage;

    public String getIdType() {
        return IdType;
    }

    public void setIdType(String idType) {
        IdType = idType;
    }

    public List<String> getResponseGroups() {
        return ResponseGroups;
    }

    public void setResponseGroups(List<String> responseGroups) {
        ResponseGroups = responseGroups;
    }

    public String getVariationPage() {
        return VariationPage;
    }

    public void setVariationPage(String variationPage) {
        VariationPage = variationPage;
    }
}
