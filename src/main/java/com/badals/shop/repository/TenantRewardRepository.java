package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantReward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TenantRewardRepository extends JpaRepository<TenantReward, Long> {
    TenantReward findByRewardType(String rewardType);
    List<TenantReward> findAllByPointsLessThanEqual(Integer points);
}
