package com.badals.shop.service.dto;
import com.badals.shop.domain.pojo.PaymentDef;
import com.badals.shop.domain.pojo.PaymentProfile;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.tenant.Tenant} entity.
 */
@Data
public class TenantDTO implements Serializable {

    private Long id;

    private String name;

    private Long maxProducts;

    private String planName;

    private Integer discountRate;

    private Boolean active;

    private BigDecimal monthlyFee;

    private String skuPrefix;

    private LocalDate contractStartDate;

    private List<PaymentDef> publicPaymentProfile;

    private Set<MerchantDTO> merchants = new HashSet<>();

    private Set<CustomerDTO> customers = new HashSet<>();



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
