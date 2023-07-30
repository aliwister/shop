package com.badals.shop.repository;

import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantCartRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantCartRuleRepository extends JpaRepository<TenantCartRule, Long> {

    Optional<TenantCartRule> findByCoupon(String coupon);

    List<TenantCartRule> findByEnabledIsTrueAndAutoApplyIsTrueOrderByPriorityAsc();
}
