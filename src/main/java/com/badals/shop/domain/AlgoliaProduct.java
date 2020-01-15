package com.badals.shop.domain;

import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.pojo.ProductI18;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlgoliaProduct implements Serializable {

    private Long id;

    @JsonProperty("objectID")
    private Long ref;
    private Long parent;
    private Long upc;
    private String sku;
    private String title;
    private String brand;
    private String group;

    private Map<String, ProductI18> i18 = new HashMap<>();
    private Map<String, BigDecimal> price;
    private String image;

    private LocalDate releaseDate;

    @NotNull
    private Boolean active;

    @Lob
    private List<String> similarProducts;

    private String url;

    private Condition condition;
    private Boolean isUsed;
    private Boolean availableForOrder;

    private BigDecimal weight;
    private BigDecimal volumeWeight;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public Long getRef() {
        return ref;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public Long getParent() {
        return parent;
    }

    public void setParent(Long parent) {
        this.parent = parent;
    }

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
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

    public Map<String, ProductI18> getI18() {
        return i18;
    }

    public void setI18(Map<String, ProductI18> i18) {
        this.i18 = i18;
    }

    public Map<String, BigDecimal> getPrice() {
        return price;
    }

    public void setPrice(Map<String, BigDecimal> price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getSimilarProducts() {
        return similarProducts;
    }

    public void setSimilarProducts(List<String> similarProducts) {
        this.similarProducts = similarProducts;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public Boolean getAvailableForOrder() {
        return availableForOrder;
    }

    public void setAvailableForOrder(Boolean availableForOrder) {
        this.availableForOrder = availableForOrder;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getVolumeWeight() {
        return volumeWeight;
    }

    public void setVolumeWeight(BigDecimal volumeWeight) {
        this.volumeWeight = volumeWeight;
    }
}
