package com.badals.shop.repository;

import com.badals.shop.domain.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Shipment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    @Query(value="Select s.id from shipment s join shop.jhi_order o on s.reference = o.reference and s.shipment_status = 'PENDING' and s.shipment_type='CUSTOMER' where o.id = :id", nativeQuery=true)
    Long findCustomerShipmentIdNative(@Param(value = "id") Long orderId);
}
