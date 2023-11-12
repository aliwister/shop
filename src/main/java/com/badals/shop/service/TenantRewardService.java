package com.badals.shop.service;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Point;
import com.badals.shop.domain.PointUsageHistory;
import com.badals.shop.domain.tenant.TenantReward;
import com.badals.shop.repository.PointRepository;
import com.badals.shop.repository.PointUsageHistoryRepository;
import com.badals.shop.repository.TenantRewardRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class TenantRewardService {

    private final Logger log = LoggerFactory.getLogger(RewardService.class);

    private final TenantRewardRepository rewardRepository;
    private final PointUsageHistoryRepository pointUsageHistoryRepository;
    private final PointRepository pointRepository;
    private final CustomerService customerService;

    public TenantRewardService(TenantRewardRepository rewardRepository, PointUsageHistoryRepository pointUsageHistoryRepository, PointRepository pointRepository, CustomerService customerService) {
        this.rewardRepository = rewardRepository;
        this.pointUsageHistoryRepository = pointUsageHistoryRepository;
        this.pointRepository = pointRepository;
        this.customerService = customerService;
    }

    @Transactional
    public List<TenantReward> getAffordableRewards(){
        Customer loginUser = customerService.getUserWithAuthorities().orElse(null);
        log.info(loginUser.getId().toString());
        if(loginUser == null)
            return new ArrayList<>();
        Integer points = getPointsForCustomer(loginUser.getId());
        return rewardRepository.findAllByPointsLessThanEqual(points);
    }

    private Integer getPointsForCustomer(Long customerId){
        List<Point> earnedPoints = pointRepository.findAllByCustomerId(customerId);
        List<PointUsageHistory> usedPoints = pointUsageHistoryRepository.findAllByCustomerId(customerId);
        Integer earned = earnedPoints.stream().mapToInt(Point::getAmount).sum();
        Integer used = usedPoints.stream().mapToInt(PointUsageHistory::getPoints).sum();
        return earned - used;
    }
}

