package com.badals.shop.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A PurchaseItem.
 */
@Entity
@Table(name = "purchase_item")
public class PurchaseItem implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "quantity", precision = 21, scale = 2)
    private BigDecimal quantity;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "estimated_delivery_date")
    private LocalDate estimatedDeliveryDate;

    @Column(name = "shipping_instructions")
    private String shippingInstructions;

    @Column(name = "description")
    private String description;

    @Column(name = "comment")
    private String comment;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("purchaseItems")
    private Purchase purchase;

    @ManyToMany
    @JoinTable(name = "purchase_item_order_item",
               joinColumns = @JoinColumn(name = "purchase_item_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "order_item_id", referencedColumnName = "id"))
    private Set<OrderItem> orderItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSequence() {
        return sequence;
    }

    public PurchaseItem sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public PurchaseItem quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public PurchaseItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public LocalDate getEstimatedDeliveryDate() {
        return estimatedDeliveryDate;
    }

    public PurchaseItem estimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
        return this;
    }

    public void setEstimatedDeliveryDate(LocalDate estimatedDeliveryDate) {
        this.estimatedDeliveryDate = estimatedDeliveryDate;
    }

    public String getShippingInstructions() {
        return shippingInstructions;
    }

    public PurchaseItem shippingInstructions(String shippingInstructions) {
        this.shippingInstructions = shippingInstructions;
        return this;
    }

    public void setShippingInstructions(String shippingInstructions) {
        this.shippingInstructions = shippingInstructions;
    }

    public String getDescription() {
        return description;
    }

    public PurchaseItem description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getComment() {
        return comment;
    }

    public PurchaseItem comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Purchase getPurchase() {
        return purchase;
    }

    public PurchaseItem purchase(Purchase purchase) {
        this.purchase = purchase;
        return this;
    }

    public void setPurchase(Purchase purchase) {
        this.purchase = purchase;
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems;
    }

    public PurchaseItem orderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
        return this;
    }

    public PurchaseItem addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
        //orderItem.getPurchaseItems().add(this);
        return this;
    }

    public PurchaseItem removeOrderItem(OrderItem orderItem) {
        this.orderItems.remove(orderItem);
        //orderItem.getPurchaseItems().remove(this);
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
        if (!(o instanceof PurchaseItem)) {
            return false;
        }
        return id != null && id.equals(((PurchaseItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PurchaseItem{" +
            "id=" + getId() +
            ", sequence=" + getSequence() +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", estimatedDeliveryDate='" + getEstimatedDeliveryDate() + "'" +
            ", shippingInstructions='" + getShippingInstructions() + "'" +
            ", description='" + getDescription() + "'" +
            ", comment='" + getComment() + "'" +
            "}";
    }
}
