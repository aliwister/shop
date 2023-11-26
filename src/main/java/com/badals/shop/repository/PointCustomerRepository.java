package com.badals.shop.repository;

import com.badals.shop.domain.PointCustomer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the PointCustomer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PointCustomerRepository extends JpaRepository<PointCustomer, Long> {
    PointCustomer findByCustomerId(Long customerId);
}
