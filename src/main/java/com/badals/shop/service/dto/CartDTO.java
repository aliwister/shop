package com.badals.shop.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.badals.shop.domain.enumeration.CartState;

/**
 * A DTO for the {@link com.badals.shop.domain.Cart} entity.
 */
public class CartDTO implements Serializable {

    private Long id;

    @NotNull
    private String secureKey;

    private Boolean gift;

    private String giftMessage;

    @NotNull
    private CartState cartState;


    private Long deliveryAddressId;

    private Long invoiceAddressId;

    private Long customerId;

    private Long currencyId;

    private Long carrierId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public Boolean isGift() {
        return gift;
    }

    public void setGift(Boolean gift) {
        this.gift = gift;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public CartState getCartState() {
        return cartState;
    }

    public void setCartState(CartState cartState) {
        this.cartState = cartState;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long addressId) {
        this.deliveryAddressId = addressId;
    }

    public Long getInvoiceAddressId() {
        return invoiceAddressId;
    }

    public void setInvoiceAddressId(Long addressId) {
        this.invoiceAddressId = addressId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long currencyId) {
        this.currencyId = currencyId;
    }

    public Long getCarrierId() {
        return carrierId;
    }

    public void setCarrierId(Long carrierId) {
        this.carrierId = carrierId;
    }

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
            ", gift='" + isGift() + "'" +
            ", giftMessage='" + getGiftMessage() + "'" +
            ", cartState='" + getCartState() + "'" +
            ", deliveryAddress=" + getDeliveryAddressId() +
            ", invoiceAddress=" + getInvoiceAddressId() +
            ", customer=" + getCustomerId() +
            ", currency=" + getCurrencyId() +
            ", carrier=" + getCarrierId() +
            "}";
    }
}
