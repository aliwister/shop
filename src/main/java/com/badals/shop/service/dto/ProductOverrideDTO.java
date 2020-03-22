package com.badals.shop.service.dto;
import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.badals.shop.domain.enumeration.OverrideType;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.shop.domain.ProductOverride} entity.
 */
@Data
public class ProductOverrideDTO implements Serializable {

    public ProductOverrideDTO() {
    }

    public ProductOverrideDTO(@NotNull String sku, @NotNull OverrideType type, String override, @NotNull Boolean active, @NotNull Boolean lazy) {
        this.sku = sku;
        this.type = type;
        this.override = override;
        this.active = active;
        this.lazy = lazy;
    }

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

            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", createdBy=" + getCreatedById() +
            ", createdBy='" + getCreatedByLogin() + "'" +
            ", lastModifiedBy=" + getLastModifiedById() +
            ", lastModifiedBy='" + getLastModifiedByLogin() + "'" +
            "}";
    }
}
