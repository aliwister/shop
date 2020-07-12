package com.badals.shop.domain;


import com.badals.shop.domain.enumeration.ShipmentStatus;

import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


/**
 * A Shipment.
 */
@Entity
@Table(name = "shipment", catalog = "admin")
//@org.springframework.data.elasticsearch.annotations.Document(indexName = "shipment")
public class Shipment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @org.springframework.data.elasticsearch.annotations.Field(type = FieldType.Keyword)
    private Long id;

    @Column(name = "estimated_ship_date")
    private LocalDate estimatedShipDate;

    @Column(name = "estimated_ready_date")
    private LocalDate estimatedReadyDate;

    @Column(name = "estimated_arrival_date")
    private LocalDate estimatedArrivalDate;

    @Column(name = "estimated_ship_cost", precision = 21, scale = 2)
    private BigDecimal estimatedShipCost;

    @Column(name = "actual_ship_cost", precision = 21, scale = 2)
    private BigDecimal actualShipCost;

    @Column(name = "latest_cancel_date")
    private LocalDate latestCancelDate;

    @Column(name = "handling_instructions")
    private String handlingInstructions;

    @Column(name = "reference")
    private String reference;

    @Column(name = "tracking_num")
    private String trackingNum;

    @Column(name = "tracking_link")
    private String trackingLink;

    @Column(name = "shipment_method")
    private String shipmentMethod;


    @Enumerated(EnumType.STRING)
    @Column(name = "shipment_status")
    private ShipmentStatus shipmentStatus;

    @OneToMany(mappedBy = "shipment", cascade=CascadeType.ALL, orphanRemoval = true)
    @OrderBy("eventDate")
    private Set<ShipmentTracking> shipmentTrackings = new HashSet<>();

    public Set<ShipmentTracking> getShipmentTrackings() {
        return shipmentTrackings;
    }

    public void setShipmentTrackings(Set<ShipmentTracking> shipmentTrackings) {
        this.shipmentTrackings = shipmentTrackings;
    }
    public Shipment addShipmentTracking(ShipmentTracking t) {
        this.shipmentTrackings.add(t);
        t.setShipment(this);
        return this;
    }


    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public LocalDate getEstimatedShipDate() {
        return estimatedShipDate;
    }

    public Shipment estimatedShipDate(LocalDate estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
        return this;
    }

    public void setEstimatedShipDate(LocalDate estimatedShipDate) {
        this.estimatedShipDate = estimatedShipDate;
    }

    public LocalDate getEstimatedReadyDate() {
        return estimatedReadyDate;
    }

    public Shipment estimatedReadyDate(LocalDate estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
        return this;
    }

    public void setEstimatedReadyDate(LocalDate estimatedReadyDate) {
        this.estimatedReadyDate = estimatedReadyDate;
    }

    public LocalDate getEstimatedArrivalDate() {
        return estimatedArrivalDate;
    }

    public Shipment estimatedArrivalDate(LocalDate estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
        return this;
    }

    public void setEstimatedArrivalDate(LocalDate estimatedArrivalDate) {
        this.estimatedArrivalDate = estimatedArrivalDate;
    }

    public BigDecimal getEstimatedShipCost() {
        return estimatedShipCost;
    }

    public Shipment estimatedShipCost(BigDecimal estimatedShipCost) {
        this.estimatedShipCost = estimatedShipCost;
        return this;
    }

    public void setEstimatedShipCost(BigDecimal estimatedShipCost) {
        this.estimatedShipCost = estimatedShipCost;
    }

    public BigDecimal getActualShipCost() {
        return actualShipCost;
    }

    public Shipment actualShipCost(BigDecimal actualShipCost) {
        this.actualShipCost = actualShipCost;
        return this;
    }

    public void setActualShipCost(BigDecimal actualShipCost) {
        this.actualShipCost = actualShipCost;
    }

    public LocalDate getLatestCancelDate() {
        return latestCancelDate;
    }

    public Shipment latestCancelDate(LocalDate latestCancelDate) {
        this.latestCancelDate = latestCancelDate;
        return this;
    }

    public void setLatestCancelDate(LocalDate latestCancelDate) {
        this.latestCancelDate = latestCancelDate;
    }

    public String getHandlingInstructions() {
        return handlingInstructions;
    }

    public Shipment handlingInstructions(String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
        return this;
    }

    public void setHandlingInstructions(String handlingInstructions) {
        this.handlingInstructions = handlingInstructions;
    }

    public String getReference() {
        return reference;
    }

    public Shipment reference(String reference) {
        this.reference = reference;
        return this;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getTrackingNum() {
        return trackingNum;
    }

    public Shipment trackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
        return this;
    }

    public void setTrackingNum(String trackingNum) {
        this.trackingNum = trackingNum;
    }

    public String getTrackingLink() {
        return trackingLink;
    }

    public Shipment trackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
        return this;
    }

    public void setTrackingLink(String trackingLink) {
        this.trackingLink = trackingLink;
    }

    public String getShipmentMethod() {
        return shipmentMethod;
    }

    public Shipment shipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
        return this;
    }

    public void setShipmentMethod(String shipmentMethod) {
        this.shipmentMethod = shipmentMethod;
    }



    public ShipmentStatus getShipmentStatus() {
        return shipmentStatus;
    }

    public Shipment shipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
        return this;
    }

    public void setShipmentStatus(ShipmentStatus shipmentStatus) {
        this.shipmentStatus = shipmentStatus;
    }


    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Shipment)) {
            return false;
        }
        return id != null && id.equals(((Shipment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Shipment{" +
            "id=" + getId() +
            ", estimatedShipDate='" + getEstimatedShipDate() + "'" +
            ", estimatedReadyDate='" + getEstimatedReadyDate() + "'" +
            ", estimatedArrivalDate='" + getEstimatedArrivalDate() + "'" +
            ", estimatedShipCost=" + getEstimatedShipCost() +
            ", actualShipCost=" + getActualShipCost() +
            ", latestCancelDate='" + getLatestCancelDate() + "'" +
            ", handlingInstructions='" + getHandlingInstructions() + "'" +
            ", reference='" + getReference() + "'" +
            ", trackingNum='" + getTrackingNum() + "'" +
            ", trackingLink='" + getTrackingLink() + "'" +
            ", shipmentMethod='" + getShipmentMethod() + "'" +
            ", shipmentStatus='" + getShipmentStatus() + "'" +
            "}";
    }
}
