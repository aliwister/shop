package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Address;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.OrderState;

import com.badals.shop.domain.pojo.AddressPojo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.hibernate.envers.NotAudited;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * A Order.
 */
@Entity
@Data
@Table(catalog="profileshop", name = "jhi_order")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantOrder implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reference")
    private String reference;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "delivery_date")
    private LocalDate deliveryDate;


    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private OrderState orderState;

    @Column(name = "currency")
    private String currency;

    //@ManyToOne
    //@JsonIgnoreProperties("orders")
    @NotAudited
    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name="customer_id", referencedColumnName = "id_customer")
    private Customer customer;

    @OneToOne
    @JoinColumn(unique = true)
    private TenantCheckout cart;

    @NotAudited
    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "delivery_address_id",referencedColumnName = "id_address")
    private Address deliveryAddress;

    @CreatedDate
    @Column(name = "created_date")
    private Date createdDate;

    @NotAudited
    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "invoice_address_id",referencedColumnName = "id_address")
    private Address invoiceAddress;

    @Column(name = "confirmation_key")
    private String confirmationKey;

    @Column(name = "subtotal")
    private BigDecimal subtotal;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "taxes_total")
    private BigDecimal taxesTotal;

    @Column(name = "delivery_total")
    private BigDecimal deliveryTotal;

    @Column(name = "discounts_total")
    private BigDecimal discountsTotal;

    @Column(name = "coupon_name")
    private BigDecimal couponName;

    @Column(name="tenant_id")
    private String tenantId;


    @Column
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotAudited
    @Type(type = "json")
    @Column(name = "delivery_address", columnDefinition = "string")
    private AddressPojo deliveryAddressPojo;


    public AddressPojo getDeliveryAddressPojo() {
        return deliveryAddressPojo;
    }

    public void setDeliveryAddressPojo(AddressPojo deliveryAddressPojo) {
        this.deliveryAddressPojo = deliveryAddressPojo;
    }



    @Column
    private String carrier;

    @Column
    private String paymentMethod;



    /*@ManyToOne
        @JsonIgnoreProperties("orders")
        private Address deliveryAddress;

        @ManyToOne
        @JsonIgnoreProperties("orders")
        private Address invoiceAddress;
        */
    @OneToMany(mappedBy = "order", cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<TenantOrderItem> orderItems = new HashSet<>();

    public TenantOrder reference(String reference) {
        this.reference = reference;
        return this;
    }

    public TenantOrder invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public TenantOrder deliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public TenantOrder currency(String currency) {
        this.currency = currency;
        return this;
    }

    public TenantOrder cart(TenantCheckout cart) {
        this.cart = cart;
        return this;
    }

/*
    public TenantOrder deliveryAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }
*/

    public TenantOrder invoiceAddress(Address address) {
        this.invoiceAddress = address;
        return this;
    }

    public TenantOrder orderItems(Set<TenantOrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public TenantOrder addOrderItem(TenantOrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public TenantOrder removeOrderItem(TenantOrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantOrder)) {
            return false;
        }
        return id != null && id.equals(((TenantOrder) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TenantOrder{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", state='" + getOrderState() + "'" +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
