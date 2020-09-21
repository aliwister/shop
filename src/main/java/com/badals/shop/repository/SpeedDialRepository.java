package com.badals.shop.repository;

import com.badals.shop.domain.SpeedDial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the SpeedDial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpeedDialRepository extends JpaRepository<SpeedDial, Long> {
   @Query
   Optional<SpeedDial> findByDial(String dial);
}
