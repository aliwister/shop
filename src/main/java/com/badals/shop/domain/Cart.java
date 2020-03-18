package com.badals.shop.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badals.shop.domain.enumeration.CartState;

/**
 * A Cart.
 */
@Entity
@Table(name = "cart")
public class Cart implements Serializable {

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
    private List<CartItem> cartItems = new ArrayList<>();

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<CartItem> cartItems) {
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

    public Cart secureKey(String secureKey) {
        this.secureKey = secureKey;
        return this;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public Boolean isGift() {
        return gift;
    }

    public Cart gift(Boolean gift) {
        this.gift = gift;
        return this;
    }

    public void setGift(Boolean gift) {
        this.gift = gift;
    }

    public String getGiftMessage() {
        return giftMessage;
    }

    public Cart giftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
        return this;
    }

    public void setGiftMessage(String giftMessage) {
        this.giftMessage = giftMessage;
    }

    public CartState getCartState() {
        return cartState;
    }

    public Cart cartState(CartState cartState) {
        this.cartState = cartState;
        return this;
    }

    public void setCartState(CartState cartState) {
        this.cartState = cartState;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Cart deliveryAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public Cart invoiceAddress(Address address) {
        this.invoiceAddress = address;
        return this;
    }

    public void setInvoiceAddress(Address address) {
        this.invoiceAddress = address;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Cart customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Currency getCurrency() {
        return currency;
    }

    public Cart currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public Cart carrier(Carrier carrier) {
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

    public Cart addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        return this;
    }

    public Cart removeCartItem(CartItem cartItem) {
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
        if (!(o instanceof Cart)) {
            return false;
        }
        return id != null && id.equals(((Cart) o).id);
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
