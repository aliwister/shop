package com.badals.shop.xtra.amazon.mappings;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
//@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageDefNode {
   @JacksonXmlProperty(localName = "URL")
   String URL;

   @JacksonXmlProperty(localName = "Height")
   String Height;

   @JacksonXmlProperty(localName = "Width")
   String Width;

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getHeight() {
        return Height;
    }

    public void setHeight(String height) {
        Height = height;
    }

    public String getWidth() {
        return Width;
    }

    public void setWidth(String width) {
        Width = width;
    }
}
