package com.badals.shop.service.dto;
import com.badals.shop.domain.enumeration.OrderState;
import lombok.Data;

import java.time.LocalDate;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Purchase} entity.
 */
@Data
public class PurchaseDTO implements Serializable {

    private Long id;

    @NotNull
    private Long po;

    private String ref;

    private String shippingInstructions;

    private String currency;

    private LocalDate invoiceDate;

    private LocalDate estimatedDeliveryDate;

    private LocalDate orderDate;

    private BigDecimal subtotal;

    private BigDecimal deliveryTotal;

    private BigDecimal taxesTotal;

    private BigDecimal discountTotal;

    private BigDecimal total;


    private Long deliveryAddressId;

    private String deliveryAddressId_address;

    private Long invoiceAddressId;

    private String invoiceAddressId_address;

    private Long merchantId;


    private OrderState orderState;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PurchaseDTO purchaseDTO = (PurchaseDTO) o;
        if (purchaseDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), purchaseDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PurchaseDTO{" +
            "id=" + getId() +
            ", po=" + getPo() +
            ", ref='" + getRef() + "'" +
            ", shippingInstructions='" + getShippingInstructions() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", subtotal=" + getSubtotal() +
            ", deliveryTotal=" + getDeliveryTotal() +
            ", taxesTotal=" + getTaxesTotal() +
            ", discountTotal=" + getDiscountTotal() +
            ", total=" + getTotal() +
            ", deliveryAddress=" + getDeliveryAddressId() +
            ", deliveryAddress='" + getDeliveryAddressId_address() + "'" +
            ", invoiceAddress=" + getInvoiceAddressId() +
            ", invoiceAddress='" + getInvoiceAddressId_address() + "'" +
            ", merchant=" + getMerchantId() +
            "}";
    }
}
