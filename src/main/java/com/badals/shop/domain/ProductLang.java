package com.badals.shop.domain;
import com.badals.shop.xtra.IProductLang;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.List;

/**
 * A ProductLang.
 */
@Entity
@Table(name = "product_lang")
public class ProductLang implements Serializable, IProductLang {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "model")
    private String model;

    @Type(type = "json")
    @Column(name = "features", columnDefinition = "string")
    private List<String> features;

    @NotNull
    @Column(name = "lang", nullable = false)
    private String lang;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("productLangs")
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ProductLang title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public ProductLang description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getModel() {
        return model;
    }

    public ProductLang model(String model) {
        this.model = model;
        return this;
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

    public ProductLang lang(String lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Product getProduct() {
        return product;
    }

    public ProductLang product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductLang)) {
            return false;
        }
        return id != null && id.equals(((ProductLang) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProductLang{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", model='" + getModel() + "'" +
            ", features='" + getFeatures() + "'" +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
