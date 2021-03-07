package com.badals.shop.service.dto;
import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.CartRule} entity.
 */
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityPerUser() {
        return quantityPerUser;
    }

    public void setQuantityPerUser(Integer quantityPerUser) {
        this.quantityPerUser = quantityPerUser;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean isPartialUse() {
        return partialUse;
    }

    public void setPartialUse(Boolean partialUse) {
        this.partialUse = partialUse;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getMinimumAmountCurrency() {
        return minimumAmountCurrency;
    }

    public void setMinimumAmountCurrency(String minimumAmountCurrency) {
        this.minimumAmountCurrency = minimumAmountCurrency;
    }

    public BigDecimal getReductionPercent() {
        return reductionPercent;
    }

    public void setReductionPercent(BigDecimal reductionPercent) {
        this.reductionPercent = reductionPercent;
    }

    public BigDecimal getReductionAmount() {
        return reductionAmount;
    }

    public void setReductionAmount(BigDecimal reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    public String getReductionCurrency() {
        return reductionCurrency;
    }

    public void setReductionCurrency(String reductionCurrency) {
        this.reductionCurrency = reductionCurrency;
    }

    public Long getReductionProduct() {
        return reductionProduct;
    }

    public void setReductionProduct(Long reductionProduct) {
        this.reductionProduct = reductionProduct;
    }

    public Boolean isHighlight() {
        return highlight;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getHashtagRestrictionId() {
        return hashtagRestrictionId;
    }

    public void setHashtagRestrictionId(Long hashtagId) {
        this.hashtagRestrictionId = hashtagId;
    }

    public String getHashtagRestrictionEn() {
        return hashtagRestrictionEn;
    }

    public void setHashtagRestrictionEn(String hashtagEn) {
        this.hashtagRestrictionEn = hashtagEn;
    }

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
            ", partialUse='" + isPartialUse() + "'" +
            ", code='" + getCode() + "'" +
            ", minimumAmount=" + getMinimumAmount() +
            ", minimumAmountCurrency='" + getMinimumAmountCurrency() + "'" +
            ", reductionPercent=" + getReductionPercent() +
            ", reductionAmount=" + getReductionAmount() +
            ", reductionCurrency='" + getReductionCurrency() + "'" +
            ", reductionProduct=" + getReductionProduct() +
            ", highlight='" + isHighlight() + "'" +
            ", active='" + isActive() + "'" +
            ", customer=" + getCustomerId() +
            ", hashtagRestriction=" + getHashtagRestrictionId() +
            ", hashtagRestriction='" + getHashtagRestrictionEn() + "'" +
            "}";
    }
}
