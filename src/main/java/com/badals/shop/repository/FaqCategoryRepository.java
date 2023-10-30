package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantFaqCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqCategoryRepository extends JpaRepository<TenantFaqCategory,Long> {
    TenantFaqCategory findTenantFaqCategoryByPosition(Integer position);
    List<TenantFaqCategory> findAllByTenantId(String tenant_id);
}
