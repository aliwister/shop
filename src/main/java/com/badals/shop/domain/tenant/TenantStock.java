package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.pojo.PriceMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantStock implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


   @Column(name="tenant_id")
   private String tenantId;

   //@NotNull
    @Column(name = "quantity", precision = 21, scale = 2, nullable = false)
    private BigDecimal quantity = new BigDecimal(0);

    //@NotNull
    @Column(name = "availability", nullable = false)
    private Integer availability = 0;

    //@NotNull
    @Column(name = "allow_backorder", nullable = false)
    private Boolean allow_backorder = false;

    @Column(name = "backorder_availability")
    private Integer backorder_availability;


    @Column(name = "location")
    private String location;

    @Column(name = "store")
    private String store;

    @Type(type = "json")
    @Column(name = "cost", columnDefinition = "string")
    private Price cost;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("merchantStocks")
    private TenantProduct product;

   // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }


    public BigDecimal getQuantity() {
        return quantity;
    }

    public TenantStock quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public Integer getAvailability() {
        return availability;
    }

    public TenantStock availability(Integer availability) {
        this.availability = availability;
        return this;
    }

    public void setAvailability(Integer availability) {
        this.availability = availability;
    }

    public Boolean isAllow_backorder() {
        return allow_backorder;
    }

    public TenantStock allow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
        return this;
    }

    public void setAllow_backorder(Boolean allow_backorder) {
        this.allow_backorder = allow_backorder;
    }

    public Integer getBackorder_availability() {
        return backorder_availability;
    }

    public TenantStock backorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
        return this;
    }

    public void setBackorder_availability(Integer backorder_availability) {
        this.backorder_availability = backorder_availability;
    }

    public String getLocation() {
        return location;
    }

    public TenantStock location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStore() {
        return store;
    }

    public TenantStock store(String store) {
        this.store = store;
        return this;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public Price getCost() {
        return cost;
    }

    public TenantStock cost(Price cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(Price cost) {
        this.cost = cost;
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
    public TenantProduct getProduct() {
        return product;
    }

    public TenantStock product(TenantProduct product) {
        this.product = product;
        return this;
    }

    public void setProduct(TenantProduct product) {
        this.product = product;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantStock)) {
            return false;
        }
        return id != null && id.equals(((TenantStock) o).id);
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
            "}";
    }
}
