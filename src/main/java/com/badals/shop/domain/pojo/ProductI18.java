package com.badals.shop.domain.pojo;

/**
 * A DTO for the {@link com.badals.shop.domain.Product} entity.
 */

public class ProductI18 {
    String title;
    String brand;
    String group;

    public ProductI18() {
    }

    public ProductI18(String title, String brand, String group) {
        this.title = title;
        this.brand = brand;
        this.group = group;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }
//List<Attribute> variationAttributes;
}
