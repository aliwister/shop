package com.badals.shop.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.domain.pojo.OrderAdjustment;
import lombok.Data;

/**
 * A DTO for the {@link com.badals.shop.domain.Cart} entity.
 */
@Data
public class CartDTO implements Serializable {

    private Long id;

    @NotNull
    private String secureKey;

    private Boolean gift;

    private String giftMessage;

    private List<CartItemDTO> cartItems = new ArrayList<>();

    //@NotNull
    private CartState cartState;


    private Long deliveryAddressId;

    private Long invoiceAddressId;

    private Long customerId;

    private Long carrierId;

    private String currency;

    private List<OrderAdjustment> adjustments = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartDTO cartDTO = (CartDTO) o;
        if (cartDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartDTO{" +
            "id=" + getId() +
            ", secureKey='" + getSecureKey() + "'" +
            ", giftMessage='" + getGiftMessage() + "'" +
            ", cartState='" + getCartState() + "'" +
            ", deliveryAddress=" + getDeliveryAddressId() +
            ", invoiceAddress=" + getInvoiceAddressId() +
            ", customer=" + getCustomerId() +
            ", currency=" + getCurrency() +
            ", carrier=" + getCarrierId() +
            "}";
    }
}
