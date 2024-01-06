package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantStockRepository extends JpaRepository<TenantStock, Long> {

}
