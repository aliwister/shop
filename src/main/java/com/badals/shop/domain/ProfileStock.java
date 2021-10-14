package com.badals.shop.domain;

import com.badals.shop.domain.pojo.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * A MerchantStock.
 */
@Entity
@Table(name = "stock", catalog = "profileshop")
@SelectBeforeUpdate(false)
public class ProfileStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //@NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    //@NotNull
    @Column(name = "availability", nullable = false)
    private Integer availability;

    //@NotNull
    @Column(name = "allow_backorder", nullable = false)
    private Boolean allow_backorder;

    @Column(name = "backorder_availability")
    private Integer backorder_availability;


    @Column(name = "location")
    private String location;

    @Column(name = "store")
    private String store;

    @Type(type = "json")
    @Column(name = "cost", columnDefinition = "string")
    private Price cost;

    @Type(type = "json")
    @Column(name = "price", columnDefinition = "string")
    private Price price;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("merchantStocks")
    private ProfileProduct product;

   @Type(type = "json")
   @Column(name = "prices", columnDefinition = "string")
   List<Price> prices;

   public List<Price> getPrices() {
      return prices;
   }

   public void setPrices(List<Price> prices) {
      this.prices = prices;
   }

   // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public ProfileStock quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailability() {
        return availability;
    }

    public ProfileStock availability(Integer availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Boolean isAllow_backorder() {
        return allow_backorder;
    }

    public ProfileStock allow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
        return this;
    }

    public void setAllow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
    }

    public Integer getBackorder_availability() {
        return backorder_availability;
    }

    public ProfileStock backorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
        return this;
    }

    public void setBackorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
    }

    public String getLocation() {
        return location;
    }

    public ProfileStock location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStore() {
        return store;
    }

    public ProfileStock store(String store) {
        this.store = store;
        return this;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Price getCost() {
        return cost;
    }

    public ProfileStock cost(Price cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Price cost) {
        this.cost = cost;
    }

    public Price getPrice() {
        return price;
    }

    public ProfileStock price(Price price) {
        this.price = price;
        return this;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

 /*   public Merchant getMerchant() {
        return merchant;
    }

    public MerchantStock merchant(Merchant merchant) {
        this.merchant = merchant;
        return this;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }
*/
    public ProfileProduct getProduct() {
        return product;
    }

    public ProfileStock product(ProfileProduct product) {
        this.product = product;
        return this;
    }

    public void setProduct(ProfileProduct product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProfileStock)) {
            return false;
        }
        return id != null && id.equals(((ProfileStock) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "MerchantStock{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", availability=" + getAvailability() +
            ", allow_backorder='" + isAllow_backorder() + "'" +
            ", backorder_availability=" + getBackorder_availability() +
            ", location='" + getLocation() + "'" +
            ", store='" + getStore() + "'" +
            ", cost='" + getCost() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
