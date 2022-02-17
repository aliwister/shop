package com.badals.shop.domain;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A PointCustomer.
 */
@Entity
@Table(name = "point_customer")
public class PointCustomer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "total_points")
    private Long totalPoints;

    @Column(name = "spent_points")
    private Long spentPoints;

    @Column(name = "referral_code")
    private String referralCode;

    @Column(name = "spent_money")
    private Double spentMoney;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @Column(name = "date_add")
    private LocalDate date_add;

    @ManyToOne(optional = true)
    @JoinColumn(name="customer_id", referencedColumnName = "id_customer", nullable = true)
    private Customer customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTotalPoints() {
        return totalPoints;
    }

    public PointCustomer totalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
        return this;
    }

    public void setTotalPoints(Long totalPoints) {
        this.totalPoints = totalPoints;
    }

    public Long getSpentPoints() {
        return spentPoints;
    }

    public PointCustomer spentPoints(Long spentPoints) {
        this.spentPoints = spentPoints;
        return this;
    }

    public void setSpentPoints(Long spentPoints) {
        this.spentPoints = spentPoints;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public PointCustomer referralCode(String referralCode) {
        this.referralCode = referralCode;
        return this;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public Double getSpentMoney() {
        return spentMoney;
    }

    public PointCustomer spentMoney(Double spentMoney) {
        this.spentMoney = spentMoney;
        return this;
    }

    public void setSpentMoney(Double spentMoney) {
        this.spentMoney = spentMoney;
    }

    public Boolean isActive() {
        return active;
    }

    public PointCustomer active(Boolean active) {
        this.active = active;
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public LocalDate getDate_add() {
        return date_add;
    }

    public PointCustomer date_add(LocalDate date_add) {
        this.date_add = date_add;
        return this;
    }

    public void setDate_add(LocalDate date_add) {
        this.date_add = date_add;
    }

    public Customer getCustomer() {
        return customer;
    }

    public PointCustomer customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PointCustomer)) {
            return false;
        }
        return id != null && id.equals(((PointCustomer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PointCustomer{" +
            "id=" + getId() +
            ", totalPoints=" + getTotalPoints() +
            ", spentPoints=" + getSpentPoints() +
            ", referralCode='" + getReferralCode() + "'" +
            ", spentMoney=" + getSpentMoney() +
            ", active='" + isActive() + "'" +
            ", date_add='" + getDate_add() + "'" +
            "}";
    }
}
