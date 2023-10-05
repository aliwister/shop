package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Address;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.CartState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Cart.
 */
@Entity
@Table(name = "wishlist", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantWishList implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("carts")
    private Customer customer;


    @Getter
    @Setter
    @Column(name = "customer_id", updatable = false, insertable = false)
    private Long customerId;


/*
    @ManyToOne
    @JsonIgnoreProperties("carts")
    private Currency currency;

  @ManyToOne
    @JsonIgnoreProperties("carts")
    private Carrier carrier;*/

    @OneToMany(mappedBy = "wishlist", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TenantWishListItem> wishlistItems = new ArrayList<TenantWishListItem>();

    @Column(name="tenant_id")
    private String tenantId;



    public List<TenantWishListItem> getWishListItems() {
        return wishlistItems;
    }

    public void setWishListItems(List<TenantWishListItem> wishlistItems) {
        this.wishlistItems = wishlistItems;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public TenantWishList customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
/*

    public Currency getCurrency() {
        return currency;
    }

    public TenantCart currency(Currency currency) {
        this.currency = currency;
        return this;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public Carrier getCarrier() {
        return carrier;
    }

    public TenantCart carrier(Carrier carrier) {
        this.carrier = carrier;
        return this;
    }

    public void setCarrier(Carrier carrier) {
        this.carrier = carrier;
    }
*/

    public void resetWishListItems() {
        this.wishlistItems = null;
        this.wishlistItems = new ArrayList<>();
    }

    public TenantWishList addCartItem(TenantWishListItem wishListItem) {
        this.wishlistItems.add(wishListItem);
        wishListItem.setWishlist(this);
        return this;
    }

    public TenantWishList removeCartItem(TenantWishListItem wishListItem) {
        this.wishlistItems.remove(wishListItem);
        wishListItem.setWishlist(null);
        return this;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantWishList)) {
            return false;
        }
        return id != null && id.equals(((TenantWishList) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "TenantWishList{" +
            "id=" + id +
            ", customerId=" + customerId +
            ", tenantId='" + tenantId + '\'' +
            '}';
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
