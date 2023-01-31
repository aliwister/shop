package com.badals.shop.service.dto;
import com.badals.shop.domain.enumeration.OrderChannel;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.OrderAdjustment;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;


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
@org.springframework.data.elasticsearch.annotations.Document(indexName = "jhi_order")
public class OrderDTO implements Serializable {

    @Id
    private Long id;

    private String reference;
    private String email;

    private LocalDate invoiceDate;

    private LocalDate deliveryDate;

    private OrderState orderState;

    private List<OrderAdjustment> orderAdjustments;

    private String currency;

    private String tenantId;


    private CustomerDTO customer;

    private CartDTO cart;

    private Long cartId;
    private Long customerId;
    private String cartSecureKey;

    private AddressDTO deliveryAddress;

    private AddressDTO invoiceAddress;

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> items) {
        this.items = items;
    }

    @Transient
    private List<OrderItemDTO> items;


    private BigDecimal subtotal;

    private BigDecimal total;

    private BigDecimal deliveryTotal;

    private BigDecimal discountsTotal;

    private String couponName;


    private Date createdDate;


    private String carrier;

    private OrderChannel channel;


    private String paymentMethod;

    private List<PaymentDTO> payments;
    private BigDecimal balance;

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
