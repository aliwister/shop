package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A CartItem.
 */
@Entity
@Table(name = "cart_item", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantCartItem implements Serializable, TenantSupport {

    private static final long serialVersionUID = 22L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cartItems")
    private TenantCart cart;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public TenantProduct getProduct() {
        return product;
    }

    public void setProduct(TenantProduct product) {
        this.product = product;
    }

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "ref", insertable = false, updatable = false)
    private TenantProduct product;

    @Column(name="tenant_id")
    private String tenantId;


    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    // @Column(name = "product_id")
   // private Long productId;

    //public Long getProductId() {
     //   return productId;
   // }

    //public void setProductId(Long productId) {
     //   this.productId = productId;
  //  }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public TenantCartItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TenantCart getCart() {
        return cart;
    }

    public TenantCartItem cart(TenantCart cart) {
        this.cart = cart;
        return this;
    }

    public void setCart(TenantCart cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantCartItem)) {
            return false;
        }
        return id != null && id.equals(((TenantCartItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "CartItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            "}";
    }
}
