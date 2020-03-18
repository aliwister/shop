package com.badals.shop.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

import com.badals.shop.domain.enumeration.OverrideType;

/**
 * A ProductOverride.
 */
@Entity
@Table(name = "product_override")
public class ProductOverride implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private OverrideType type;

    @Column(name = "override")
    private String override;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @NotNull
    @Column(name = "lazy", nullable = false)
    private Boolean lazy;

    @Column(name = "created_date")
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @ManyToOne
    @JsonIgnoreProperties("productOverrides")
    private User createdBy;

    @ManyToOne
    @JsonIgnoreProperties("productOverrides")
    private User lastModifiedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public ProductOverride sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public OverrideType getType() {
        return type;
    }

    public ProductOverride type(OverrideType type) {
        this.type = type;
        return this;
    }

    public void setType(OverrideType type) {
        this.type = type;
    }

    public String getOverride() {
        return override;
    }

    public ProductOverride override(String override) {
        this.override = override;
        return this;
    }

    public void setOverride(String override) {
        this.override = override;
    }

    public Boolean isActive() {
        return active;
    }

    public ProductOverride active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isLazy() {
        return lazy;
    }

    public ProductOverride lazy(Boolean lazy) {
        this.lazy = lazy;
        return this;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public ProductOverride createdDate(Instant createdDate) {
        this.createdDate = createdDate;
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public ProductOverride lastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public ProductOverride createdBy(User user) {
        this.createdBy = user;
        return this;
    }

    public void setCreatedBy(User user) {
        this.createdBy = user;
    }

    public User getLastModifiedBy() {
        return lastModifiedBy;
    }

    public ProductOverride lastModifiedBy(User user) {
        this.lastModifiedBy = user;
        return this;
    }

    public void setLastModifiedBy(User user) {
        this.lastModifiedBy = user;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductOverride)) {
            return false;
        }
        return id != null && id.equals(((ProductOverride) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ProductOverride{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", type='" + getType() + "'" +
            ", override='" + getOverride() + "'" +
            ", active='" + isActive() + "'" +
            ", lazy='" + isLazy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
