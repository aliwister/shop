package com.badals.shop.service.dto;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.CartItem} entity.
 */
@Data
public class CartItemDTO implements Serializable {

    private Long id;

    private Integer quantity;

    private Long cartId;

    private Long productId;
    private Long merchantId;

    private String price;
    private String listPrice;
    private String currency;

    private String salePrice;
    private String image;
    private String url;

    private String title;
    private String slug;
    private String unit = "pcs";

    //private List<String> variationAttributes = new ArrayList<String>();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CartItemDTO cartItemDTO = (CartItemDTO) o;
        if (cartItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), cartItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CartItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", cart=" + getCartId() +
            ", product=" + getProductId() +

            "}";
    }
}