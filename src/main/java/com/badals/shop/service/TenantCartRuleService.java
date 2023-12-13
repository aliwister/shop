package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantCartRule;
import com.badals.shop.repository.TenantCartRuleRepository;
import com.badals.shop.service.dto.TenantCartRuleDTO;
import com.badals.shop.service.mapper.TenantCartRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class TenantCartRuleService {
    private final Logger log = LoggerFactory.getLogger(TenantCartRuleService.class);

    private TenantCartRuleRepository tenantCartRuleRepository;
    private TenantCartRuleMapper tenantCartRuleMapper;

    public TenantCartRuleService(TenantCartRuleRepository tenantCartRuleRepository, TenantCartRuleMapper tenantCartRuleMapper) {
        this.tenantCartRuleRepository = tenantCartRuleRepository;
        this.tenantCartRuleMapper = tenantCartRuleMapper;
    }

    public List<TenantCartRuleDTO> getCartRules(){
        return tenantCartRuleRepository.findAll().stream().map(tenantCartRuleMapper::toDto).collect(Collectors.toList());
    }

    public TenantCartRuleDTO getCartRuleByCoupon(String coupon){
        return tenantCartRuleMapper.toDto(tenantCartRuleRepository.findByCoupon(coupon));
    }

    public TenantCartRuleDTO addCartRule(TenantCartRuleDTO tenantCartRuleDTO){
        return tenantCartRuleMapper.toDto(tenantCartRuleRepository.save(tenantCartRuleMapper.toEntity(tenantCartRuleDTO)));
    }

    public TenantCartRuleDTO updateCartRule(String coupon, TenantCartRuleDTO tenantCartRuleDTO){
        TenantCartRule tenantCartRule = tenantCartRuleRepository.findByCoupon(coupon);
        TenantCartRule tenanatCartRuleInput = tenantCartRuleMapper.toEntity(tenantCartRuleDTO);
        tenantCartRule.setCoupon(tenanatCartRuleInput.getCoupon());
        tenantCartRule.setEnabled(tenanatCartRuleInput.getEnabled());
        tenantCartRule.setAutoApply(tenanatCartRuleInput.getAutoApply());
        tenantCartRule.setCanCombine(tenanatCartRuleInput.getCanCombine());
        tenantCartRule.setPriority(tenanatCartRuleInput.getPriority());
        tenantCartRule.setDescription(tenanatCartRuleInput.getDescription());
        tenantCartRule.setRules(tenanatCartRuleInput.getRules());
        tenantCartRule.setCheckoutRules(tenanatCartRuleInput.getCheckoutRules());
        tenantCartRule.setReductionType(tenanatCartRuleInput.getReductionType());
        tenantCartRule.setDiscount(tenanatCartRuleInput.getDiscount());
        tenantCartRule.setDiscountType(tenanatCartRuleInput.getDiscountType());

        tenantCartRule = tenantCartRuleRepository.save(tenantCartRule);
        return tenantCartRuleMapper.toDto(tenantCartRule);
    }

    public void deleteCartRule(String coupon){
        tenantCartRuleRepository.deleteByCoupon(coupon);
    }

}
