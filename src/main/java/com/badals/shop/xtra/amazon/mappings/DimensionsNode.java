package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DimensionsNode {
   @XStreamAlias("Units")
   @XStreamAsAttribute
   String units;

   @XStreamAlias("Height")
   String height;

   @XStreamAlias("Length")
   String length ;

   @XStreamAlias("Weight")
   String weight ;

   @XStreamAlias("Width")
   String width;

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }
}
