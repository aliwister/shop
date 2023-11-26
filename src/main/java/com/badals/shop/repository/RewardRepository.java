package com.badals.shop.repository;

import com.badals.shop.domain.Reward;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Reward entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RewardRepository extends JpaRepository<Reward, Long> {
    Reward findByRewardType(String rewardType);
}
