package com.badals.shop.service.dto;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.OrderItem} entity.
 */
@Data
public class OrderItemDTO implements Serializable {

    private Long id;

    private String productName;

    private Integer quantity;

    private BigDecimal price;

    private String comment;

    private Integer sequence;

    private String shippingInstructions;

    private String image;


    private BigDecimal weight;


    private String unit;


    private BigDecimal lineTotal;


    private Long orderId;

private String sku;
private String url;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderItemDTO orderItemDTO = (OrderItemDTO) o;
        if (orderItemDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderItemDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderItemDTO{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", comment='" + getComment() + "'" +
            ", sequence=" + getSequence() +
            ", shippingInstructions='" + getShippingInstructions() + "'" +
            ", order=" + getOrderId() +
            "}";
    }
}
