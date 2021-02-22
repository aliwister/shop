package com.badals.shop.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.ProductLang} entity.
 */
public class ProductLangDTO implements Serializable {

    private Long id;

    private String title;

    private String description;

    private String model;
    private String brand;

    private List<String> features;

    @NotNull
    private String lang;


    private Long productId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductLangDTO productLangDTO = (ProductLangDTO) o;
        if (productLangDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productLangDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductLangDTO{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", model='" + getModel() + "'" +
            ", features='" + getFeatures() + "'" +
            ", lang='" + getLang() + "'" +
            ", product=" + getProductId() +
            "}";
    }
}
