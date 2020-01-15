package com.badals.shop.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.*;
import javax.persistence.Lob;

import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.domain.pojo.VariationOption;

/**
 * A DTO for the {@link com.badals.shop.domain.Product} entity.
 */
public class ProductDTO implements Serializable {

    private Long id;

    private Long ref;

    private Long parent;

    @NotNull
    private String sku;

    private Long upc;

    @NotNull
    private String price;

    @NotNull
    private String currency;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private String image;

    @Lob
    private List<String> images;

    private LocalDate releaseDate;

    @NotNull
    private Boolean active;

    @Lob
    private List<String> similarProducts;

    private String url;

    @NotNull
    private String title;

    private String brand;

    private String group;

    //@NotNull
    //private Instant updated;

    //@NotNull
    //private Instant created;

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

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Long getUpc() {
        return upc;
    }

    public void setUpc(Long upc) {
        this.upc = upc;
    }


    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean isActive() {
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

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public Boolean isIsUsed() {
        return isUsed;
    }

    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    public Boolean isAvailableForOrder() {
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


    // Variations

    List<String> variationDimensions;
    List<VariationOption> variationOptions;
    List<Variation> variations;
    List<Attribute> variationAttributes;

    List<ProductLang> i18n;

    public List<String> getVariationDimensions() {
        return variationDimensions;
    }

    public void setVariationDimensions(List<String> variationDimensions) {
        this.variationDimensions = variationDimensions;
    }

    public List<VariationOption> getVariationOptions() {
        return variationOptions;
    }

    public void setVariationOptions(List<VariationOption> variationOptions) {
        this.variationOptions = variationOptions;
    }

    public List<Variation> getVariations() {
        return variations;
    }

    public void setVariations(List<Variation> variations) {
        this.variations = variations;
    }

    public List<Attribute> getVariationAttributes() {
        return variationAttributes;
    }

    public void setVariationAttributes(List<Attribute> variationAttributes) {
        this.variationAttributes = variationAttributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (productDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", ref=" + getRef() +
            ", parent=" + getParent() +
            ", sku='" + getSku() + "'" +
            ", upc=" + getUpc() +
            ", price=" + getPrice() +
            ", image='" + getImage() + "'" +
            ", images='" + getImages() + "'" +
            ", releaseDate='" + getReleaseDate() + "'" +
            ", active='" + isActive() + "'" +
            ", similarProducts='" + getSimilarProducts() + "'" +
            ", url='" + getUrl() + "'" +
            ", title='" + getTitle() + "'" +
            ", brand='" + getBrand() + "'" +
            ", group='" + getGroup() + "'" +
            ", condition='" + getCondition() + "'" +
            ", isUsed='" + isIsUsed() + "'" +
            ", availableForOrder='" + isAvailableForOrder() + "'" +
            ", weight=" + getWeight() +
            ", volumeWeight=" + getVolumeWeight() +
            "}";
    }
}
