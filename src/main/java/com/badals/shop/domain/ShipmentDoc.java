package com.badals.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;

/**
 * A ShipmentDoc.
 */
@Entity
@Table(name = "shipment_doc", catalog = "admin")
public class ShipmentDoc implements Serializable {

    private static final long serialVersionUID = 1L;

    public ShipmentDoc() {
    }



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "file_key")
    private String fileKey;

    @ManyToOne
    @JsonIgnoreProperties("shipmentDocs")
    private Shipment shipment;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileKey() {
        return fileKey;
    }

    public ShipmentDoc fileKey(String fileKey) {
        this.fileKey = fileKey;
        return this;
    }

    public void setFileKey(String fileKey) {
        this.fileKey = fileKey;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public ShipmentDoc shipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentDoc)) {
            return false;
        }
        return id != null && id.equals(((ShipmentDoc) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipmentDoc{" +
            "id=" + getId() +
            ", fileKey='" + getFileKey() + "'" +
            "}";
    }
}
