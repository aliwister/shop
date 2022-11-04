package com.badals.shop.repository;


import com.badals.shop.domain.tenant.TenantOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the TenantOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantOrderItemRepository extends JpaRepository<TenantOrderItem, Long> {

}
