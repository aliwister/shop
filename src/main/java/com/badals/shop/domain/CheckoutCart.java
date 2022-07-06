package com.badals.shop.domain;


import com.badals.shop.domain.pojo.AddressPojo;
import com.badals.shop.domain.pojo.LineItem;
import com.badals.shop.domain.pojo.OrderAdjustment;
import com.badals.shop.domain.pojo.PaymentMethod;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.NaturalId;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.envers.NotAudited;

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
@Table(catalog="shop", name = "checkout_cart")
public class CheckoutCart implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String ref;
    private String name;
    private String phone;
    private String email;


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

    private String currency;

    @Type(type = "json")
    @Column(name = "cart", columnDefinition = "string")
    List<LineItem> items;

    @Column(name="tenant_id")
    private Long tenantId;

    @Column(name="allow_pickup")
    private Boolean allowPickup = false;

    @Column(name="checked_out")
    private Boolean checkedOut = false;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CheckoutCart)) {
            return false;
        }
        return id != null && id.equals(((CheckoutCart) o).id);
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
}
