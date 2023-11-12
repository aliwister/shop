package com.badals.shop.repository;

import com.badals.shop.domain.PointUsageHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointUsageHistoryRepository extends JpaRepository<PointUsageHistory, Long> {
    List<PointUsageHistory> findAllByCustomerId(Long customerId);
    PointUsageHistory findByCheckoutIdAndRewardId(Long checkoutId, Long rewardId);
    void removeByCheckoutIdAndRewardId(Long checkoutId, Long rewardId);
}
