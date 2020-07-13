package com.badals.shop.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * A ShipmentEvent.
 */
@Entity
@Table(name = "shipment_event")
public class ShipmentEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "ref", nullable = false)
    private Integer ref;

    @NotNull
    @Column(name = "lang", nullable = false)
    private String lang;

    @Column
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRef() {
        return ref;
    }

    public ShipmentEvent ref(Integer ref) {
        this.ref = ref;
        return this;
    }

    public void setRef(Integer ref) {
        this.ref = ref;
    }

    public String getLang() {
        return lang;
    }

    public ShipmentEvent lang(String lang) {
        this.lang = lang;
        return this;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentEvent)) {
            return false;
        }
        return id != null && id.equals(((ShipmentEvent) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipmentEvent{" +
            "id=" + getId() +
            ", ref=" + getRef() +
            ", lang='" + getLang() + "'" +
            "}";
    }
}
