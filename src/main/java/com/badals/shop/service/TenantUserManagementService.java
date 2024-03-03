package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.UserBase;
import com.badals.shop.domain.tenant.TenantAuthority;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.repository.TenantAuthorityRepository;
import com.badals.shop.service.dto.CustomerInfoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class TenantUserManagementService {

    private final Logger log = LoggerFactory.getLogger(TenantUserManagementService.class);

    private final CustomerRepository customerRepository;
    private final TenantAuthorityRepository tenantAuthorityRepository;

    public TenantUserManagementService(CustomerRepository customerRepository, TenantAuthorityRepository tenantAuthorityRepository) {
        this.customerRepository = customerRepository;
        this.tenantAuthorityRepository = tenantAuthorityRepository;
    }

    public List<CustomerInfoDTO> getTenantModerators(){
        Map<Long,List<TenantAuthority>> tenantAuthorities = tenantAuthorityRepository.getAllByTenantId(TenantContext.getCurrentTenant()).stream().collect(Collectors.groupingBy(TenantAuthority::getUserId));
        TenantContext.setCurrentProfile("profileshop");
        TenantContext.setCurrentTenant("profileshop");
        List<Customer> moderators = customerRepository.findAllByIdIn(tenantAuthorities.keySet());
        return moderators.stream().map(customer -> CustomerInfoDTO.builder()
            .firstname(customer.getFirstname())
            .lastname(customer.getLastname())
            .email(customer.getEmail())
            .roles(tenantAuthorities.get(customer.getId()).stream().map(TenantAuthority::getAuthority).collect(Collectors.toList()))
            .build()).collect(Collectors.toList());

    }
}
