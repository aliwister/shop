package com.badals.shop.service.dto;
import lombok.Data;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.Instant;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.SpeedDial} entity.
 */
@Data
@Accessors(fluent = true)
public class SpeedDialDTO implements Serializable {

    private Long id;

    @NotNull
    private String dial;

    @NotNull
    private Long ref;

    @NotNull
    private Instant expires;


    private Long productId;

    private String productRef;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDial() {
        return dial;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }

    public Long getRef() {
        return ref;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public Instant getExpires() {
        return expires;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductRef() {
        return productRef;
    }

    public void setProductRef(String productRef) {
        this.productRef = productRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        SpeedDialDTO speedDialDTO = (SpeedDialDTO) o;
        if (speedDialDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), speedDialDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SpeedDialDTO{" +
            "id=" + getId() +
            ", dial='" + getDial() + "'" +
            ", ref='" + getRef() + "'" +
            ", expires='" + getExpires() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductRef() + "'" +
            "}";
    }
}
