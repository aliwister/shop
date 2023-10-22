package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantFaqQA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqQARepository extends JpaRepository<TenantFaqQA,Long> {
    List<TenantFaqQA> findAllByTenantId(String tenantId);
    TenantFaqQA findTenantFaqQAByCategoryIdAndTenantIdAndPosition(Long categoryId, String tenantId, Integer position);
}
