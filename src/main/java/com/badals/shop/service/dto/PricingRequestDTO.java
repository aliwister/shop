package com.badals.shop.service.dto;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.PricingRequest} entity.
 */
@Data
public class PricingRequestDTO implements Serializable {

    private Long id;

    @NotNull
    private String sku;

    private String ref;
    private String email;
    private String parent;

    private int merchantId;
    private String merchantName;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PricingRequestDTO pricingRequestDTO = (PricingRequestDTO) o;
        if (pricingRequestDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pricingRequestDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PricingRequestDTO{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            "}";
    }
}
