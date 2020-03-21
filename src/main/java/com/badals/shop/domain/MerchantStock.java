package com.badals.shop.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A MerchantStock.
 */
@Entity
@Table(name = "merchant_stock")
@SelectBeforeUpdate(false)
public class MerchantStock implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity;

    @NotNull
    @Column(name = "availability", nullable = false)
    private Integer availability;

    @NotNull
    @Column(name = "allow_backorder", nullable = false)
    private Boolean allow_backorder;

    @Column(name = "backorder_availability")
    private Integer backorder_availability;

    @Column(name = "link")
    private String link;

    @Column(name = "location")
    private String location;

    @Column(name = "store")
    private String store;

   // @Type(type = "json")
    //@Column(name = "cost", columnDefinition = "string")
    private BigDecimal cost;

    //@Type(type = "json")
    //@Column(name = "price", columnDefinition = "string")
    private BigDecimal price;


    private Integer discount;

   public Integer getDiscount() {
      return discount;
   }

   public void setDiscount(Integer discount) {
      this.discount = discount;
   }
   public MerchantStock discount(Integer discount) {
      this.discount = discount;
      return this;
   }
   //@ManyToOne(optional = false)
   // @NotNull
   // @JsonIgnoreProperties("merchantStocks")
   // private Merchant merchant;

   public Long getMerchantId() {
      return merchantId;
   }

   public void setMerchantId(Long merchantId) {
      this.merchantId = merchantId;
   }
   @Column(name = "merchant_id")
   private Long merchantId;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("merchantStocks")
    private Product product;



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

    public MerchantStock quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailability() {
        return availability;
    }

    public MerchantStock availability(Integer availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Boolean isAllow_backorder() {
        return allow_backorder;
    }

    public MerchantStock allow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
        return this;
    }

    public void setAllow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
    }

    public Integer getBackorder_availability() {
        return backorder_availability;
    }

    public MerchantStock backorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
        return this;
    }

    public void setBackorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
    }

    public String getLink() {
        return link;
    }

    public MerchantStock link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation() {
        return location;
    }

    public MerchantStock location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStore() {
        return store;
    }

    public MerchantStock store(String store) {
        this.store = store;
        return this;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public MerchantStock cost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public MerchantStock price(BigDecimal price) {
        this.price = price;
        return this;
    }

    public void setPrice(BigDecimal price) {
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
    public Product getProduct() {
        return product;
    }

    public MerchantStock product(Product product) {
        this.product = product;
        return this;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MerchantStock)) {
            return false;
        }
        return id != null && id.equals(((MerchantStock) o).id);
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
            ", link='" + getLink() + "'" +
            ", location='" + getLocation() + "'" +
            ", store='" + getStore() + "'" +
            ", cost='" + getCost() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }
}
