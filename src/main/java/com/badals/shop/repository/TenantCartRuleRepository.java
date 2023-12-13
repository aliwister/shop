package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantCartRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TenantCartRuleRepository extends JpaRepository<TenantCartRule, Long> {
    TenantCartRule findByCoupon(String coupon);
    void deleteByCoupon(String coupon);
}
