package com.badals.shop.domain.tenant;

import com.badals.shop.domain.Address;
import com.badals.shop.domain.Carrier;
import com.badals.shop.domain.Currency;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.CartState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart", catalog = "profileshop")
public class TenantCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "secure_key", nullable = false, unique = true)
    private String secureKey;

    @Column(name = "gift")
    private Boolean gift;

    @Column(name = "gift_message")
    private String giftMessage;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cart_state", nullable = false)
    private CartState cartState = CartState.UNCLAIMED;

    @ManyToOne
    @JsonIgnoreProperties("carts")
    @JoinColumn(name = "delivery_address_id")
    private Address deliveryAddress;

    @ManyToOne
    @JsonIgnoreProperties("carts")
    @JoinColumn(name = "invoice_address_id")
    private Address invoiceAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("carts")
    private Customer customer;


    @ManyToOne
    @JsonIgnoreProperties("carts")
    private Currency currency;

    @ManyToOne
    @JsonIgnoreProperties("carts")
    private Carrier carrier;

    @OneToMany(mappedBy = "cart", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TenantCartItem> cartItems = new ArrayList<TenantCartItem>();

    public List<TenantCartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<TenantCartItem> cartItems) {
        this.cartItems = cartItems;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSecureKey() {
        return secureKey;
    }

    public TenantCart secureKey(String secureKey) {
        this.secureKey = secureKey;
        return this;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public Boolean isGift() {
        return gift;
    }

    public TenantCart gift(Boolean gift) {
        this.gift = gift;
        return this;
    }

    public void setGift(Boolean gift) {
        this.gift = gift;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public TenantCart giftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
        return this;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public CartState getCartState() {
        return cartState;
    }

    public TenantCart cartState(CartState cartState) {
        this.cartState = cartState;
        return this;
    }

    public void setCartState(CartState cartState) {
        this.cartState = cartState;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public TenantCart deliveryAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public TenantCart invoiceAddress(Address address) {
        this.invoiceAddress = address;
        return this;
    }

    public void setInvoiceAddress(Address address) {
        this.invoiceAddress = address;
    }


    public Customer getCustomer() {
        return customer;
    }

    public TenantCart customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Currency getCurrency() {
        return currency;
    }

    public TenantCart currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public TenantCart carrier(Carrier carrier) {
        this.carrier = carrier;
        return this;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }

    public void resetCartItems() {
        this.cartItems = null;
        this.cartItems = new ArrayList<>();
    }

    public TenantCart addCartItem(TenantCartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        return this;
    }

    public TenantCart removeCartItem(TenantCartItem cartItem) {
        this.cartItems.remove(cartItem);
        cartItem.setCart(null);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantCart)) {
            return false;
        }
        return id != null && id.equals(((TenantCart) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +
            ", secureKey='" + getSecureKey() + "'" +
            ", gift='" + isGift() + "'" +
            ", giftMessage='" + getGiftMessage() + "'" +
            ", cartState='" + getCartState() + "'" +
            "}";
    }
}
