package com.badals.shop.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.badals.shop.domain.enumeration.OverrideType;

/**
 * A DTO for the {@link com.badals.shop.domain.ProductOverride} entity.
 */
public class ProductOverrideDTO implements Serializable {

    private Long id;

    @NotNull
    private String sku;

    @NotNull
    private OverrideType type;

    private String override;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean lazy;

    private Instant createdDate;

    private Instant lastModifiedDate;


    private Long createdById;

    private String createdByLogin;

    private Long lastModifiedById;

    private String lastModifiedByLogin;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public OverrideType getType() {
        return type;
    }

    public void setType(OverrideType type) {
        this.type = type;
    }

    public String getOverride() {
        return override;
    }

    public void setOverride(String override) {
        this.override = override;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean isLazy() {
        return lazy;
    }

    public void setLazy(Boolean lazy) {
        this.lazy = lazy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long userId) {
        this.createdById = userId;
    }

    public String getCreatedByLogin() {
        return createdByLogin;
    }

    public void setCreatedByLogin(String userLogin) {
        this.createdByLogin = userLogin;
    }

    public Long getLastModifiedById() {
        return lastModifiedById;
    }

    public void setLastModifiedById(Long userId) {
        this.lastModifiedById = userId;
    }

    public String getLastModifiedByLogin() {
        return lastModifiedByLogin;
    }

    public void setLastModifiedByLogin(String userLogin) {
        this.lastModifiedByLogin = userLogin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ProductOverrideDTO productOverrideDTO = (ProductOverrideDTO) o;
        if (productOverrideDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), productOverrideDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ProductOverrideDTO{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", type='" + getType() + "'" +
            ", override='" + getOverride() + "'" +
            ", active='" + isActive() + "'" +
            ", lazy='" + isLazy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy=" + getCreatedById() +
            ", createdBy='" + getCreatedByLogin() + "'" +
            ", lastModifiedBy=" + getLastModifiedById() +
            ", lastModifiedBy='" + getLastModifiedByLogin() + "'" +
            "}";
    }
}
