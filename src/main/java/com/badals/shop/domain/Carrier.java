package com.badals.shop.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A Carrier.
 */
@Entity
@Table(name = "carrier")
public class Carrier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ref")
    private String ref;

    @Column(name = "name")
    private String name;

    @Column(name = "max_weight", precision = 21, scale = 2)
    private BigDecimal maxWeight;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public Carrier ref(String ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getName() {
        return name;
    }

    public Carrier name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public Carrier maxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
        return this;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Carrier)) {
            return false;
        }
        return id != null && id.equals(((Carrier) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Carrier{" +
            "id=" + getId() +
            ", ref='" + getRef() + "'" +
            ", name='" + getName() + "'" +
            ", maxWeight=" + getMaxWeight() +
            "}";
    }
}
