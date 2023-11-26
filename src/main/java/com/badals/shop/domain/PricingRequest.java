package com.badals.shop.domain;

import com.badals.shop.domain.tenant.TenantProduct;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Date;

/**
 * A PricingRequest.
 */
@Entity
@Table(name = "pricing_request")
public class PricingRequest extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @Column(name = "done")
    private Boolean done;

    @Column(name = "email_sent")
    private Boolean emailSent;

    @ManyToOne
    private Merchant merchant;

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sku", referencedColumnName = "ref", insertable = false, updatable = false)
    private TenantProduct product;

    public TenantProduct getProduct() {
        return product;
    }

    public void setProduct(TenantProduct product) {
        this.product = product;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public PricingRequest sku(String sku) {
        this.sku = sku;
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PricingRequest)) {
            return false;
        }
        return id != null && id.equals(((PricingRequest) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PricingRequest{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            "}";
    }
}
