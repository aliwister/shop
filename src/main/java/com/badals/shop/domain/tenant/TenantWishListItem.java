package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A WishlistItem.
 */
@Entity
@Table(name = "whishlist_item", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantWishListItem implements Serializable, TenantSupport {

    private static final long serialVersionUID = 22L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("wishlistItems")
    private TenantWishList wishlist;

    @Getter
    @Setter
    @Column(name = "wishlist_id", updatable = false, insertable = false)
    private Long wishlistId;

    public TenantWishListItem() {

    }


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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
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

    public TenantWishListItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public TenantWishList getWishList() {
        return wishlist;
    }

    public TenantWishListItem wishlist(TenantWishList tenantWishList) {
        this.wishlist = tenantWishList;
        return this;
    }

    public void setWishlist(TenantWishList tenantWishList) {
        this.wishlist = tenantWishList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantWishListItem)) {
            return false;
        }
        return id != null && id.equals(((TenantWishListItem) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TenantWishListItem{" +
            "id=" + id +
            ", quantity=" + quantity +
            ", productId=" + productId +
            ", tenantId='" + tenantId + '\'' +
            '}';
    }
}
