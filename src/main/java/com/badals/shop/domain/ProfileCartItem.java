package com.badals.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A CartItem.
 */
@Entity
@Table(name = "cart_item", catalog = "profileshop")
public class ProfileCartItem implements Serializable {

    private static final long serialVersionUID = 22L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("cartItems")
    private ProfileCart cart;


    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public ProfileProduct getProduct() {
        return product;
    }

    public void setProduct(ProfileProduct product) {
        this.product = product;
    }

    @Column(name = "product_id")
    private Long productId;

    @ManyToOne
    @JoinColumn(name="product_id", referencedColumnName = "ref", insertable = false, updatable = false)
    private ProfileProduct product;
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

    public ProfileCartItem quantity(Integer quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public ProfileCart getCart() {
        return cart;
    }

    public ProfileCartItem cart(ProfileCart cart) {
        this.cart = cart;
        return this;
    }

    public void setCart(ProfileCart cart) {
        this.cart = cart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileCartItem)) {
            return false;
        }
        return id != null && id.equals(((ProfileCartItem) o).id);
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
