package com.badals.shop.domain;
import com.badals.shop.domain.enumeration.OrderState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Purchase.
 */
@Entity
@Table(catalog="shop", name = "purchase")
public class Purchase extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref")
    private String ref;

    @Column(name = "shipping_instructions")
    private String shippingInstructions;

    @Column(name = "currency")
    private String currency;

    @Column(name = "invoice_date")
    private LocalDate invoiceDate;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "order_date")
    private LocalDate orderDate;

    @Column(name = "subtotal", precision = 21, scale = 2)
    private BigDecimal subtotal;

    @Column(name = "delivery_total", precision = 21, scale = 2)
    private BigDecimal deliveryTotal;

    @Column(name = "taxes_total", precision = 21, scale = 2)
    private BigDecimal taxesTotal;

    @Column(name = "discount_total", precision = 21, scale = 2)
    private BigDecimal discountTotal;

    @Column(name = "total", precision = 21, scale = 2)
    private BigDecimal total;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "delivery_address_id",referencedColumnName = "id_address")
    private Address deliveryAddress;

    @ManyToOne
    @JsonIgnoreProperties("orders")
    @JoinColumn(name = "invoice_address_id",referencedColumnName = "id_address")
    private Address invoiceAddress;

    @ManyToOne()
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_state")
    private OrderState orderState;

    @OneToMany(mappedBy = "purchase",cascade=CascadeType.ALL, orphanRemoval = true)
    private Set<PurchaseItem> purchaseItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public Purchase ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getShippingInstructions() {
        return shippingInstructions;
    }

    public Purchase shippingInstructions(String shippingInstructions) {
        this.shippingInstructions = shippingInstructions;
        return this;
    }

    public void setShippingInstructions(String shippingInstructions) {
        this.shippingInstructions = shippingInstructions;
    }

    public String getCurrency() {
        return currency;
    }

    public Purchase currency(String currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public LocalDate getInvoiceDate() {
        return invoiceDate;
    }

    public Purchase invoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
        return this;
    }

    public void setInvoiceDate(LocalDate invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public Purchase estimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        return this;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public Purchase orderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
        return this;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public Purchase subtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getDeliveryTotal() {
        return deliveryTotal;
    }

    public Purchase deliveryTotal(BigDecimal deliveryTotal) {
        this.deliveryTotal = deliveryTotal;
        return this;
    }

    public void setDeliveryTotal(BigDecimal deliveryTotal) {
        this.deliveryTotal = deliveryTotal;
    }

    public BigDecimal getTaxesTotal() {
        return taxesTotal;
    }

    public Purchase taxesTotal(BigDecimal taxesTotal) {
        this.taxesTotal = taxesTotal;
        return this;
    }

    public void setTaxesTotal(BigDecimal taxesTotal) {
        this.taxesTotal = taxesTotal;
    }

    public BigDecimal getDiscountTotal() {
        return discountTotal;
    }

    public Purchase discountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
        return this;
    }

    public void setDiscountTotal(BigDecimal discountTotal) {
        this.discountTotal = discountTotal;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Purchase total(BigDecimal total) {
        this.total = total;
        return this;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Address getDeliveryAddress() {
        return deliveryAddress;
    }

    public Purchase deliveryAddress(Address address) {
        this.deliveryAddress = address;
        return this;
    }

    public void setDeliveryAddress(Address address) {
        this.deliveryAddress = address;
    }

    public Address getInvoiceAddress() {
        return invoiceAddress;
    }

    public Purchase invoiceAddress(Address address) {
        this.invoiceAddress = address;
        return this;
    }

    public void setInvoiceAddress(Address address) {
        this.invoiceAddress = address;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public Purchase merchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public Purchase orderState(OrderState orderState) {
        this.orderState = orderState;
        return this;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public Set<PurchaseItem> getPurchaseItems() {
        return purchaseItems;
    }

    public Purchase purchaseItems(Set<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
        return this;
    }

    public Purchase addPurchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItems.add(purchaseItem);
        purchaseItem.setPurchase(this);
        return this;
    }

    public Purchase removePurchaseItem(PurchaseItem purchaseItem) {
        this.purchaseItems.remove(purchaseItem);
        purchaseItem.setPurchase(null);
        return this;
    }

    public void setPurchaseItems(Set<PurchaseItem> purchaseItems) {
        this.purchaseItems = purchaseItems;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Purchase)) {
            return false;
        }
        return id != null && id.equals(((Purchase) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Purchase{" +
            "id=" + getId() +
            ", ref='" + getRef() + "'" +
            ", shippingInstructions='" + getShippingInstructions() + "'" +
            ", currency='" + getCurrency() + "'" +
            ", invoiceDate='" + getInvoiceDate() + "'" +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", orderDate='" + getOrderDate() + "'" +
            ", subtotal=" + getSubtotal() +
            ", deliveryTotal=" + getDeliveryTotal() +
            ", taxesTotal=" + getTaxesTotal() +
            ", discountTotal=" + getDiscountTotal() +
            ", total=" + getTotal() +
            "}";
    }
}
