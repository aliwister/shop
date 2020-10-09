package com.badals.shop.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.badals.shop.domain.enumeration.DiscountReductionType;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.shop.domain.Reward} entity.
 */
@Data
public class RewardDTO implements Serializable {

    private Long id;

    private String rewardType;

    @NotNull
    private Long points;

    private Long radius;

    private Long minimumCartAmount;

    private Long discountValue;

    private Long discountValidDays;

    @NotNull
    private DiscountReductionType discountReductionType;


    private Long productId;

    private String productRef;

    private String name;
    private String description;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RewardDTO rewardDTO = (RewardDTO) o;
        if (rewardDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rewardDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RewardDTO{" +
            "id=" + getId() +
            ", rewardType='" + getRewardType() + "'" +
            ", points=" + getPoints() +
            ", radius=" + getRadius() +
            ", minimumCartAmount=" + getMinimumCartAmount() +
            ", discountValue=" + getDiscountValue() +
            ", discountValidDays=" + getDiscountValidDays() +
            ", discountReductionType='" + getDiscountReductionType() + "'" +
            ", product=" + getProductId() +
            ", product='" + getProductRef() + "'" +
            "}";
    }
}
