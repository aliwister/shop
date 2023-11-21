package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.pojo.*;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.*;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
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
@Table(catalog = "profileshop", name = "checkout")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Checkout implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NaturalId
    private String ref;
    private String name;
    private String phone;
    private String email;

    @Column(name="guest")
    private Boolean guest;

    @Column(name="secure_key")
    private String secureKey;

    @Type(type = "json")
    @Column(name = "delivery_address", columnDefinition = "string")
    private AddressPojo deliveryAddress;

    @Column(name = "delivery_address_id", columnDefinition = "string")
    private Long deliveryAddressId;

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

    @Column(name = "cart_weight")
    private BigDecimal cartWeight;

    private String payment;

    private String currency;

    @Type(type = "json")
    @Column(name = "checkout_content", columnDefinition = "string")
    List<LineItem> items;

    @Column(name="tenant_id")
    private String tenantId;

    @Column(name="payment_token")
    private String paymentToken;

    @Column(name="checked_out")
    private Boolean checkedOut;

    @Column(name="allow_pickup")
    private Boolean allowPickup;

    @NotAudited
    @Type(type = "json")
    @Column(name = "adjustments", columnDefinition = "string")
    private List<OrderAdjustment> orderAdjustments;

    @Type(type = "json")
    @Column(name="new_adjustments", columnDefinition = "string")
    private List<CheckoutAdjustmentProfile> adjustments = new ArrayList<>();


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Checkout)) {
            return false;
        }
        return id != null && id.equals(((Checkout) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Checkout{" +
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
                ", guest=" + guest +
                '}';
    }
}
