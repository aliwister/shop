package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantFaqQA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FaqQARepository extends JpaRepository<TenantFaqQA,Long> {
    TenantFaqQA findTenantFaqQAByCategoryIdAndPosition(Long categoryId, Integer position);
}
