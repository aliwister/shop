package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Product;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A OrderItem.
 */
@Entity
@Data
@Table(catalog="profileshop", name = "order_item")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantOrderItem implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "quantity")
    private BigDecimal quantity;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Column(name = "cost", precision = 21, scale = 2)
    private BigDecimal cost;

    @Column(name = "comment")
    private String comment;

    @Column(name = "sequence")
    private Integer sequence;

    @Column(name = "shipping_instructions")
    private String shippingInstructions;

    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "product_id", referencedColumnName = "ref", updatable=false, insertable=false)
    private TenantProduct product;


    @Column(name="product_id") String ref;

    @Column
    private String image;

    @Column
    private BigDecimal weight;

    @Column
    private String unit;

    @Column String sku;


    @Column(name="line_total")
    private BigDecimal lineTotal;


    public void setOrder(TenantOrder order) {
        this.order = order;
    }

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("orderItems")
    private TenantOrder order;

    public TenantOrderItem productName(String productName) {
        this.productName = productName;
        return this;
    }

    public TenantOrderItem quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public TenantOrderItem price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public TenantOrderItem comment(String comment) {
        this.comment = comment;
        return this;
    }

    public TenantOrderItem sequence(Integer sequence) {
        this.sequence = sequence;
        return this;
    }

    public TenantOrderItem shippingInstructions(String shippingInstructions) {
        this.shippingInstructions = shippingInstructions;
        return this;
    }

    public TenantOrder getOrder() {
        return order;
    }

    public TenantOrderItem order(TenantOrder order) {
        this.order = order;
        return this;
    }
    public TenantOrderItem lineTotal(double doubleValue) {
        this.lineTotal = BigDecimal.valueOf(doubleValue);
        return this;
    }

    @Column(name="tenant_id")
    private String tenantId;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantOrderItem)) {
            return false;
        }
        return id != null && id.equals(((TenantOrderItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TenantOrderItem{" +
            "id=" + getId() +
            ", productName='" + getProductName() + "'" +
            ", quantity=" + getQuantity() +
            ", price=" + getPrice() +
            ", comment='" + getComment() + "'" +
            ", sequence=" + getSequence() +
            ", shippingInstructions='" + getShippingInstructions() + "'" +
            "}";
    }

    public BigDecimal getCost() {
        return cost;
    }


   public TenantOrderItem product(TenantProduct p) {
        this.setProduct(p);
        return this;
   }

    public TenantOrderItem ref(String ref) {
        this.setRef(ref);
        return this;
    }
}
