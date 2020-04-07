package com.badals.shop.service.dto;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Tenant} entity.
 */
public class TenantDTO implements Serializable {

    private Long id;

    private String name;


    private Set<MerchantDTO> merchants = new HashSet<>();

    private Set<CustomerDTO> customers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<MerchantDTO> getMerchants() {
        return merchants;
    }

    public void setMerchants(Set<MerchantDTO> merchants) {
        this.merchants = merchants;
    }

    public Set<CustomerDTO> getCustomers() {
        return customers;
    }

    public void setCustomers(Set<CustomerDTO> customers) {
        this.customers = customers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TenantDTO tenantDTO = (TenantDTO) o;
        if (tenantDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tenantDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TenantDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
