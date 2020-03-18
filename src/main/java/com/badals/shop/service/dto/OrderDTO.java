package com.badals.shop.service.dto;
import com.badals.shop.domain.enumeration.OrderState;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Objects;


/**
 * A DTO for the {@link com.badals.shop.domain.Order} entity.
 */
@Data
public class OrderDTO implements Serializable {

    private Long id;

    private String reference;

    private LocalDate invoiceDate;

    private LocalDate deliveryDate;

    private OrderState orderState;


    private String currency;


    private CustomerDTO customer;

    private CartDTO cart;

    private AddressDTO deliveryAddress;

    private AddressDTO invoiceAddress;

    private List<OrderItemDTO> orderItems;


    private BigDecimal subtotal;

    private BigDecimal total;

    private BigDecimal deliveryTotal;

    private BigDecimal discountsTotal;


    private Date createdDate;


    private String carrier;


    private String paymentMethod;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderDTO orderDTO = (OrderDTO) o;
        if (orderDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", state='" + getOrderState() + "'" +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
