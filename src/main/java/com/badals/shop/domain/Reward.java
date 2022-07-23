package com.badals.shop.domain;
import com.badals.shop.domain.tenant.TenantProduct;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import com.badals.shop.domain.enumeration.DiscountReductionType;

/**
 * A Reward.
 */
@Entity
@Table(name = "point_reward")
public class Reward implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reward_type")
    private String rewardType;

    @NotNull
    @Column(name = "points", nullable = false)
    private Long points;

    @Column(name = "radius")
    private Long radius;

    @Column(name = "minimum_cart_amount")
    private Long minimumCartAmount;

    @Column(name = "discount_value")
    private Long discountValue;

    @Column(name = "discount_valid_days")
    private Long discountValidDays;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "discount_reduction_type", nullable = false)
    private DiscountReductionType discountReductionType;

    @ManyToOne(optional = true)
    @JsonIgnoreProperties("rewards")
    private TenantProduct product;

    @OneToMany(mappedBy = "reward")
    private Set<RewardLang> rewardLangs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRewardType() {
        return rewardType;
    }

    public Reward rewardType(String rewardType) {
        this.rewardType = rewardType;
        return this;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public Long getPoints() {
        return points;
    }

    public Reward points(Long points) {
        this.points = points;
        return this;
    }

    public void setPoints(Long points) {
        this.points = points;
    }

    public Long getRadius() {
        return radius;
    }

    public Reward radius(Long radius) {
        this.radius = radius;
        return this;
    }

    public void setRadius(Long radius) {
        this.radius = radius;
    }

    public Long getMinimumCartAmount() {
        return minimumCartAmount;
    }

    public Reward minimumCartAmount(Long minimumCartAmount) {
        this.minimumCartAmount = minimumCartAmount;
        return this;
    }

    public void setMinimumCartAmount(Long minimumCartAmount) {
        this.minimumCartAmount = minimumCartAmount;
    }

    public Long getDiscountValue() {
        return discountValue;
    }

    public Reward discountValue(Long discountValue) {
        this.discountValue = discountValue;
        return this;
    }

    public void setDiscountValue(Long discountValue) {
        this.discountValue = discountValue;
    }

    public Long getDiscountValidDays() {
        return discountValidDays;
    }

    public Reward discountValidDays(Long discountValidDays) {
        this.discountValidDays = discountValidDays;
        return this;
    }

    public void setDiscountValidDays(Long discountValidDays) {
        this.discountValidDays = discountValidDays;
    }

    public DiscountReductionType getDiscountReductionType() {
        return discountReductionType;
    }

    public Reward discountReductionType(DiscountReductionType discountReductionType) {
        this.discountReductionType = discountReductionType;
        return this;
    }

    public void setDiscountReductionType(DiscountReductionType discountReductionType) {
        this.discountReductionType = discountReductionType;
    }

    public TenantProduct getProduct() {
        return product;
    }

    public Reward product(TenantProduct product) {
        this.product = product;
        return this;
    }

    public void setProduct(TenantProduct product) {
        this.product = product;
    }

    public Set<RewardLang> getRewardLangs() {
        return rewardLangs;
    }

    public Reward rewardLangs(Set<RewardLang> rewardLangs) {
        this.rewardLangs = rewardLangs;
        return this;
    }

    public Reward addRewardLang(RewardLang rewardLang) {
        this.rewardLangs.add(rewardLang);
        rewardLang.setReward(this);
        return this;
    }

    public Reward removeRewardLang(RewardLang rewardLang) {
        this.rewardLangs.remove(rewardLang);
        rewardLang.setReward(null);
        return this;
    }

    public void setRewardLangs(Set<RewardLang> rewardLangs) {
        this.rewardLangs = rewardLangs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reward)) {
            return false;
        }
        return id != null && id.equals(((Reward) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Reward{" +
            "id=" + getId() +
            ", rewardType='" + getRewardType() + "'" +
            ", points=" + getPoints() +
            ", radius=" + getRadius() +
            ", minimumCartAmount=" + getMinimumCartAmount() +
            ", discountValue=" + getDiscountValue() +
            ", discountValidDays=" + getDiscountValidDays() +
            ", discountReductionType='" + getDiscountReductionType() + "'" +
            "}";
    }
}
