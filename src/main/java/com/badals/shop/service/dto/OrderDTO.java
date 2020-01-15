package com.badals.shop.service.dto;
import com.badals.shop.domain.enumeration.OrderState;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the {@link com.badals.shop.domain.Order} entity.
 */
public class OrderDTO implements Serializable {

    private Long id;

    private String reference;

    private LocalDate invoiceDate;

    private LocalDate deliveryDate;

    private OrderState state;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }

    public void setCustomer(CustomerDTO customer) {
        this.customer = customer;
    }

    public CartDTO getCart() {
        return cart;
    }

    public void setCart(CartDTO cart) {
        this.cart = cart;
    }

    public AddressDTO getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(AddressDTO deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public AddressDTO getInvoiceAddress() {
        return invoiceAddress;
    }

    public void setInvoiceAddress(AddressDTO invoiceAddress) {
        this.invoiceAddress = invoiceAddress;
    }

    private String currency;


    private CustomerDTO customer;

    private CartDTO cart;

    private AddressDTO deliveryAddress;

    private AddressDTO invoiceAddress;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public OrderState getState() {
        return state;
    }

    public void setState(OrderState state) {
        this.state = state;
    }

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
            ", state='" + getState() + "'" +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
