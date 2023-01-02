package com.badals.shop.service.pojo;

import com.badals.shop.domain.pojo.Price;
import lombok.Data;
import org.mapstruct.Mapper;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.tenant.TenantStock} entity.
 */
@Data
public class PartnerStock implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal quantity;

    @NotNull
    private Integer availability;

    @NotNull
    private Boolean allow_backorder;

    private Integer backorder_availability;

    private String link;

    private String location;

    private String store;

    private Price cost;
    private Price price;

    private Long merchantId;

    private Long productId;
    private String productRef;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PartnerStock stockDTO = (PartnerStock) o;
        if (stockDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), stockDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "MerchantStockDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", availability=" + getAvailability() +
            ", allow_backorder='" + getAllow_backorder() + "'" +
            ", backorder_availability=" + getBackorder_availability() +
            ", link='" + getLink() + "'" +
            ", location='" + getLocation() + "'" +
            ", store='" + getStore() + "'" +
            ", cost='" + getCost() + "'" +
            ", price='" + getPrice() + "'" +
            ", merchant=" + getMerchantId() +
            ", product=" + getProductId() +
            "}";
    }
}
