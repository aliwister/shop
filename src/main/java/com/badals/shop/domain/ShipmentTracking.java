package com.badals.shop.domain;


import com.badals.shop.domain.enumeration.ShipmentStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A ShipmentStatus.
 */
@Entity
@Table(name = "shipment_tracking")
public class ShipmentTracking extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="event_date")
    private LocalDateTime eventDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ShipmentStatus status;

    @Column(name = "details")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties("shipmentTrackings")
    private Shipment shipment;

    @ManyToOne(optional = false)
    @JoinColumn(name="shipment_event_id", referencedColumnName = "ref", insertable = false, updatable = false)

    private ShipmentEvent shipmentEvent;

    @Column(name = "shipment_event_id")
    @NotNull
    private Integer shipmentEventId;


    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getShipmentEventId() {
        return shipmentEventId;
    }

    public void setShipmentEventId(Integer shipmentEventId) {
        this.shipmentEventId = shipmentEventId;
    }
    public ShipmentTracking shipmentEventId(Integer shipmentEventId) {
        this.shipmentEventId = shipmentEventId;
        return this;
    }
    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ShipmentStatus getStatus() {
        return status;
    }

    public ShipmentTracking status(ShipmentStatus status) {
        this.status = status;
        return this;
    }

    public ShipmentTracking eventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setStatus(ShipmentStatus status) {
        this.status = status;
    }

    public String getDetails() {
        return details;
    }

    public ShipmentTracking details(String details) {
        this.details = details;
        return this;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public Shipment getShipment() {
        return shipment;
    }

    public ShipmentTracking shipment(Shipment shipment) {
        this.shipment = shipment;
        return this;
    }

    public void setShipment(Shipment shipment) {
        this.shipment = shipment;
    }

    public ShipmentEvent getShipmentEvent() {
        return shipmentEvent;
    }

    public ShipmentTracking shipmentEvent(ShipmentEvent shipmentEvent) {
        this.shipmentEvent = shipmentEvent;
        return this;
    }

    public void setShipmentEvent(ShipmentEvent shipmentEvent) {
        this.shipmentEvent = shipmentEvent;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ShipmentTracking)) {
            return false;
        }
        return id != null && id.equals(((ShipmentTracking) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "ShipmentStatus{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", details='" + getDetails() + "'" +
            "}";
    }
}
