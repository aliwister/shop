package com.badals.shop.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;

/**
 * A SpeedDial.
 */
@Entity
@Table(name = "speed_dial")
public class SpeedDial implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "dial", nullable = false, unique = true)
    private String dial;

    @NotNull
    @Column(name = "ref", nullable = false)
    private Long ref;

    @NotNull
    @Column(name = "expires", nullable = false)
    private Instant expires;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="ref", referencedColumnName = "ref", insertable = false, updatable = false)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDial() {
        return dial;
    }

    public SpeedDial dial(String dial) {
        this.dial = dial;
        return this;
    }

    public void setDial(String dial) {
        this.dial = dial;
    }

    public Long getRef() {
        return ref;
    }

    public SpeedDial ref(Long ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(Long ref) {
        this.ref = ref;
    }

    public Instant getExpires() {
        return expires;
    }

    public SpeedDial expires(Instant expires) {
        this.expires = expires;
        return this;
    }

    public void setExpires(Instant expires) {
        this.expires = expires;
    }

    public Product getProduct() {
        return product;
    }

    public SpeedDial product(Product product) {
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
        if (!(o instanceof SpeedDial)) {
            return false;
        }
        return id != null && id.equals(((SpeedDial) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "SpeedDial{" +
            "id=" + getId() +
            ", dial='" + getDial() + "'" +
            ", ref='" + getRef() + "'" +
            ", expires='" + getExpires() + "'" +
            "}";
    }
}
