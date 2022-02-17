package com.badals.shop.service.dto;
import com.badals.shop.domain.PointCustomer;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link PointCustomer} entity.
 */
public class PointCustomerDTO implements Serializable {

    private Long id;

    private Long totalPoints;

    private Long spentPoints;

    private String referralCode;

    private Double spentMoney;

    @NotNull
    private Boolean active;

    private LocalDate date_add;


    private Long customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getSpentPoints() {
        return spentPoints;
    }

    public void setSpentPoints(Long spentPoints) {
        this.spentPoints = spentPoints;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Double getSpentMoney() {
        return spentMoney;
    }

    public void setSpentMoney(Double spentMoney) {
        this.spentMoney = spentMoney;
    }

    public Boolean isActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getDate_add() {
        return date_add;
    }

    public void setDate_add(LocalDate date_add) {
        this.date_add = date_add;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PointCustomerDTO pointCustomerDTO = (PointCustomerDTO) o;
        if (pointCustomerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pointCustomerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PointCustomerDTO{" +
            "id=" + getId() +
            ", totalPoints=" + getTotalPoints() +
            ", spentPoints=" + getSpentPoints() +
            ", referralCode='" + getReferralCode() + "'" +
            ", spentMoney=" + getSpentMoney() +
            ", active='" + isActive() + "'" +
            ", date_add='" + getDate_add() + "'" +
            ", customer=" + getCustomerId() +
            "}";
    }
}
