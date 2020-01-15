package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AvailabilityAttributesNode {
   @JacksonXmlProperty(localName = "AvailabilityType")
   String AvailabilityType;

   @JacksonXmlProperty(localName = "MinimumHours")
   String MinimumHours;

   @JacksonXmlProperty(localName = "MaximumHours")
   String MaximumHours;

    public String getAvailabilityType() {
        return AvailabilityType;
    }

    public void setAvailabilityType(String availabilityType) {
        AvailabilityType = availabilityType;
    }

    public String getMinimumHours() {
        return MinimumHours;
    }

    public void setMinimumHours(String minimumHours) {
        MinimumHours = minimumHours;
    }

    public String getMaximumHours() {
        return MaximumHours;
    }

    public void setMaximumHours(String maximumHours) {
        MaximumHours = maximumHours;
    }
}
