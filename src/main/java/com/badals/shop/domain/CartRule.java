package com.badals.shop.domain;
import com.badals.shop.domain.tenant.TenantHashtag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A CartRule.
 */
@Entity
@Table(name = "cart_rule")
public class CartRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @NotNull
    @Column(name = "quantity_per_user", nullable = false)
    private Integer quantityPerUser;

    @Column(name = "priority")
    private Integer priority;

    @NotNull
    @Column(name = "partial_use", nullable = false)
    private Boolean partialUse;

    @NotNull
    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "minimum_amount", precision = 21, scale = 2)
    private BigDecimal minimumAmount;

    @Column(name = "minimum_amount_currency")
    private String minimumAmountCurrency;

    @Column(name = "reduction_percent", precision = 21, scale = 2)
    private BigDecimal reductionPercent;

    @Column(name = "reduction_amount", precision = 21, scale = 2)
    private BigDecimal reductionAmount;

    @NotNull
    @Column(name = "reduction_currency", nullable = false)
    private String reductionCurrency;

    @Column(name = "reduction_product")
    private Long reductionProduct;

    @Column(name = "highlight")
    private Boolean highlight;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToOne
    @JsonIgnoreProperties("cartRules")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "hashtag_restriction",referencedColumnName = "name")
    @JsonIgnoreProperties("cartRules")
    private TenantHashtag hashtagRestriction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateFrom() {
        return dateFrom;
    }

    public CartRule dateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
        return this;
    }

    public void setDateFrom(LocalDate dateFrom) {
        this.dateFrom = dateFrom;
    }

    public LocalDate getDateTo() {
        return dateTo;
    }

    public CartRule dateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
        return this;
    }

    public void setDateTo(LocalDate dateTo) {
        this.dateTo = dateTo;
    }

    public String getDescription() {
        return description;
    }

    public CartRule description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public CartRule quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityPerUser() {
        return quantityPerUser;
    }

    public CartRule quantityPerUser(Integer quantityPerUser) {
        this.quantityPerUser = quantityPerUser;
        return this;
    }

    public void setQuantityPerUser(Integer quantityPerUser) {
        this.quantityPerUser = quantityPerUser;
    }

    public Integer getPriority() {
        return priority;
    }

    public CartRule priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Boolean isPartialUse() {
        return partialUse;
    }

    public CartRule partialUse(Boolean partialUse) {
        this.partialUse = partialUse;
        return this;
    }

    public void setPartialUse(Boolean partialUse) {
        this.partialUse = partialUse;
    }

    public String getCode() {
        return code;
    }

    public CartRule code(String code) {
        this.code = code;
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getMinimumAmount() {
        return minimumAmount;
    }

    public CartRule minimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
        return this;
    }

    public void setMinimumAmount(BigDecimal minimumAmount) {
        this.minimumAmount = minimumAmount;
    }

    public String getMinimumAmountCurrency() {
        return minimumAmountCurrency;
    }

    public CartRule minimumAmountCurrency(String minimumAmountCurrency) {
        this.minimumAmountCurrency = minimumAmountCurrency;
        return this;
    }

    public void setMinimumAmountCurrency(String minimumAmountCurrency) {
        this.minimumAmountCurrency = minimumAmountCurrency;
    }

    public BigDecimal getReductionPercent() {
        return reductionPercent;
    }

    public CartRule reductionPercent(BigDecimal reductionPercent) {
        this.reductionPercent = reductionPercent;
        return this;
    }

    public void setReductionPercent(BigDecimal reductionPercent) {
        this.reductionPercent = reductionPercent;
    }

    public BigDecimal getReductionAmount() {
        return reductionAmount;
    }

    public CartRule reductionAmount(BigDecimal reductionAmount) {
        this.reductionAmount = reductionAmount;
        return this;
    }

    public void setReductionAmount(BigDecimal reductionAmount) {
        this.reductionAmount = reductionAmount;
    }

    public String getReductionCurrency() {
        return reductionCurrency;
    }

    public CartRule reductionCurrency(String reductionCurrency) {
        this.reductionCurrency = reductionCurrency;
        return this;
    }

    public void setReductionCurrency(String reductionCurrency) {
        this.reductionCurrency = reductionCurrency;
    }

    public Long getReductionProduct() {
        return reductionProduct;
    }

    public CartRule reductionProduct(Long reductionProduct) {
        this.reductionProduct = reductionProduct;
        return this;
    }

    public void setReductionProduct(Long reductionProduct) {
        this.reductionProduct = reductionProduct;
    }

    public Boolean isHighlight() {
        return highlight;
    }

    public CartRule highlight(Boolean highlight) {
        this.highlight = highlight;
        return this;
    }

    public void setHighlight(Boolean highlight) {
        this.highlight = highlight;
    }

    public Boolean isActive() {
        return active;
    }

    public CartRule active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Customer getCustomer() {
        return customer;
    }

    public CartRule customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public TenantHashtag getHashtagRestriction() {
        return hashtagRestriction;
    }

    public CartRule hashtagRestriction(TenantHashtag hashtag) {
        this.hashtagRestriction = hashtag;
        return this;
    }

    public void setHashtagRestriction(TenantHashtag hashtag) {
        this.hashtagRestriction = hashtag;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CartRule)) {
            return false;
        }
        return id != null && id.equals(((CartRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CartRule{" +
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
            "}";
    }
}
