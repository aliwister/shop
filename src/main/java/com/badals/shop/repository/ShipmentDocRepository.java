package com.badals.shop.repository;

import com.badals.shop.domain.ShipmentDoc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Spring Data  repository for the ShipmentDoc entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShipmentDocRepository extends JpaRepository<ShipmentDoc, Long> {

}
