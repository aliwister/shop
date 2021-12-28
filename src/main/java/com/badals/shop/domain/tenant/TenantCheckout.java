package com.badals.shop.domain.tenant;


import com.badals.shop.domain.checkout.helper.AddressPojo;
import com.badals.shop.domain.checkout.helper.LineItem;
import com.badals.shop.domain.checkout.helper.PaymentMethod;
import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.service.dto.CartDTO;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * A Product.
 */
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
@Entity
@Data
@Table(name = "checkout", catalog = "profileshop")
public class TenantCheckout implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String ref;
    private String name;
    private String phone;
    private String email;

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "cart_state", nullable = false)
    private CartState cartState = CartState.UNCLAIMED;

    @Column(name="secure_key")
    private String secureKey;

    @Type(type = "json")
    @Column(name = "delivery_address", columnDefinition = "string")
    private AddressPojo deliveryAddress;

    @Type(type = "json")
    @Column(name = "invoice_address", columnDefinition = "string")
    private AddressPojo invoiceAddress;

    @Type(type = "json")
    @Column(name = "addresses", columnDefinition = "string")
    private List<AddressPojo> addresses;

    @Type(type = "json")
    @Column(name = "payment_methods", columnDefinition = "string")
    private List<PaymentMethod> paymentMethods;

    @Column(name = "carrier")
    private String carrier;

    @Column(name="checked_out")
    private Boolean checkedOut = false;

    private String currency;

    @Type(type = "json")
    @Column(name = "checkout_content", columnDefinition = "string")
    List<LineItem> items;

/*    @OneToMany(mappedBy = "cart", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<ProfileCartItem> cartItems = new ArrayList<ProfileCartItem>();

    public List<ProfileCartItem> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<ProfileCartItem> cartItems) {
        this.cartItems = cartItems;
    }*/

    @Column(name="tenant_id")
    private Long tenantId;

    @Column(name="allow_pickup")
    private Boolean allowPickup;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantCart)) {
            return false;
        }
        return id != null && id.equals(((TenantCheckout) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", ref='" + ref + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", secureKey='" + secureKey + '\'' +
                ", deliveryAddress=" + deliveryAddress +
                ", invoiceAddress=" + invoiceAddress +
                ", addresses=" + addresses +
                ", paymentMethods=" + paymentMethods +
                ", carrier='" + carrier + '\'' +
                ", currency='" + currency + '\'' +
                ", items=" + items +
                ", tenantId=" + tenantId +
                '}';
    }

    public CartDTO getCustomer() {
        return null;
    }

/*
    public ProfileCheckout addCartItem(ProfileCartItem cartItem) {
        this.cartItems.add(cartItem);
        cartItem.setCart(this);
        return this;
    }*/
}
