package com.badals.shop.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Carrier} entity.
 */
public class CarrierDTO implements Serializable {

    private Long id;

    private String ref;

    private String name;

    private BigDecimal maxWeight;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CarrierDTO carrierDTO = (CarrierDTO) o;
        if (carrierDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), carrierDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CarrierDTO{" +
            "id=" + getId() +
            ", ref='" + getRef() + "'" +
            ", name='" + getName() + "'" +
            ", maxWeight=" + getMaxWeight() +
            "}";
    }
}
