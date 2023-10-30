package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the OrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderItemRepository extends JpaRepository<TenantOrderItem, Long> {
   public List<TenantOrderItem> findAllByOrder(TenantOrder order);
}
