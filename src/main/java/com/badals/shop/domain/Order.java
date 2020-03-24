package com.badals.shop.domain;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.checkout.helper.AddressPojo;
import com.badals.shop.domain.enumeration.OrderState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;
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
@Table(name = "jhi_order")
@Audited
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;
   public Order() {
   }

   public Order(Long id) {
      this.id = id;
   }

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

   @NotAudited
    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name="customer_id", referencedColumnName = "id_customer")
    private Customer customer;

   @NotAudited
    @OneToOne
    @JoinColumn(unique = true)
    private CheckoutCart cart;

    @Column
    private String email;

    @NotAudited
    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "delivery_address_id",referencedColumnName = "id_address")
    private Address deliveryAddress;

   @NotAudited
    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "invoice_address_id",referencedColumnName = "id_address")
    private Address invoiceAddress;

   @NotAudited
    @Type(type = "json")
    @Column(name = "delivery_address", columnDefinition = "string")
    private AddressPojo deliveryAddressPojo;

   @NotAudited
   @Column(name="confirmation_key")
   private String confirmationKey;

   private BigDecimal subtotal;

   private BigDecimal total;

   @Column(name="delivery_total")
   private BigDecimal deliveryTotal;

   @Column(name="discounts_total")
   private BigDecimal discountsTotal;

   @CreatedDate
   @Column(name = "created_date")
   private Date createdDate;

   @Column
   private String carrier;

   @Column(name = "payment_method")
   private String paymentMethod;

@NotAudited
   @OneToMany(mappedBy = "order")
   private Set<OrderItem> orderItems = new HashSet<>();

    public AddressPojo getDeliveryAddressPojo() {
        return deliveryAddressPojo;
    }

    public void setDeliveryAddressPojo(AddressPojo deliveryAddressPojo) {
        this.deliveryAddressPojo = deliveryAddressPojo;
    }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getDeliveryTotal() {
        return deliveryTotal;
    }

    public void setDeliveryTotal(BigDecimal deliveryTotal) {
        this.deliveryTotal = deliveryTotal;
    }

    public BigDecimal getDiscountsTotal() {
        return discountsTotal;
    }

    public void setDiscountsTotal(BigDecimal discountsTotal) {
        this.discountsTotal = discountsTotal;
    }

    public String getConfirmationKey() {
        return confirmationKey;
    }

    public void setConfirmationKey(String confirmationKey) {
        this.confirmationKey = confirmationKey;
    }



    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public Order reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public Order invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public Order deliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
        return this;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Order orderState(OrderState orderState) {
        this.orderState = orderState;
        return this;
    }


    public String getCurrency() {
        return currency;
    }

    public Order currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Order customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public CheckoutCart getCart() {
        return cart;
    }

    public void setCart(CheckoutCart cart) {
        this.cart = cart;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Order deliveryAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public Order invoiceAddress(Address address) {
        this.invoiceAddress = address;
        return this;
    }

    public void setInvoiceAddress(Address address) {
        this.invoiceAddress = address;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public Order orderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public Order addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        orderItem.setOrder(this);
        return this;
    }

    public Order removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        orderItem.setOrder(null);
        return this;
    }

    public void setOrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        return id != null && id.equals(((Order) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + getId() +
            ", reference='" + getReference() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", deliveryDate='" + getDeliveryDate() + "'" +
            ", state='" + getOrderState() + "'" +
            ", currency='" + getCurrency() + "'" +
            "}";
    }
}
