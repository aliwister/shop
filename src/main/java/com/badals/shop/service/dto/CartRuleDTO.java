package com.badals.shop.service.dto;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.CartRule} entity.
 */
@Data
public class CartRuleDTO implements Serializable {

    private Long id;
    @NotNull
    private LocalDate dateFrom;
    @NotNull
    private LocalDate dateTo;
    private String description;
    @NotNull
    private Integer quantity;
    @NotNull
    private Integer quantityPerUser;
    private Integer priority;
    @NotNull
    private Boolean partialUse;
    @NotNull
    private String code;
    private BigDecimal minimumAmount;
    private String minimumAmountCurrency;
    private BigDecimal reductionPercent;
    private BigDecimal reductionAmount;
    @NotNull
    private String reductionCurrency;
    private Long reductionProduct;
    private Boolean highlight;
    @NotNull
    private Boolean active;
    private Long customerId;
    private Long hashtagRestrictionId;
    private String hashtagRestrictionEn;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartRuleDTO cartRuleDTO = (CartRuleDTO) o;
        if (cartRuleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartRuleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartRuleDTO{" +
            "id=" + getId() +
            ", dateFrom='" + getDateFrom() + "'" +
            ", dateTo='" + getDateTo() + "'" +
            ", description='" + getDescription() + "'" +
            ", quantity=" + getQuantity() +
            ", quantityPerUser=" + getQuantityPerUser() +
            ", priority=" + getPriority() +
            ", code='" + getCode() + "'" +
            ", minimumAmount=" + getMinimumAmount() +
            ", minimumAmountCurrency='" + getMinimumAmountCurrency() + "'" +
            ", reductionPercent=" + getReductionPercent() +
            ", reductionAmount=" + getReductionAmount() +
            ", reductionCurrency='" + getReductionCurrency() + "'" +
            ", reductionProduct=" + getReductionProduct() +
            ", customer=" + getCustomerId() +
            ", hashtagRestriction=" + getHashtagRestrictionId() +
            ", hashtagRestriction='" + getHashtagRestrictionEn() + "'" +
            "}";
    }
}
