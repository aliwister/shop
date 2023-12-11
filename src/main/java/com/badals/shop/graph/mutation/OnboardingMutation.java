package com.badals.shop.graph.mutation;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.domain.tenant.TenantAuthority;
import com.badals.shop.repository.TenantAuthorityRepository;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.AddSignUpResponseInput;
import com.badals.shop.service.pojo.*;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Component
public class OnboardingMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(OnboardingMutation.class);

    private final TenantAdminProductService productService;
    private final TenantCartService cartService;
    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final AwsService awsService;

    private final TenantRepository tenantRepository;
    private final TenantAuthorityRepository tenantAuthorityRepository;
    private final CustomerService customerService;
    private final PreOnBoardingService preOnBoardingService;

    @Value("${profileshop.cdnUrl}")
    private String cdnUrl;


    public OnboardingMutation(TenantAdminProductService productService, TenantCartService cartService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService, AwsService awsService, TenantRepository tenantRepository, TenantAuthorityRepository tenantAuthorityRepository, CustomerService customerService, PreOnBoardingService preOnBoardingService) {
        this.productService = productService;
        this.cartService = cartService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.awsService = awsService;
        this.tenantRepository = tenantRepository;
        this.tenantAuthorityRepository = tenantAuthorityRepository;
        this.customerService = customerService;
        this.preOnBoardingService = preOnBoardingService;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public Message createTenant(String tenantId) throws ProductNotFoundException {
        Tenant tenant = new Tenant();
        tenant.setTenantId(tenantId);
        tenant.setName(tenantId);
        tenant.setSubdomain(tenantId);
        tenant.setActive(true);
        tenant.setCreatedDate(LocalDate.now());
        tenant.setIsSubdomain(true);
        tenant.setMaxProducts(1000L);
        tenantRepository.save(tenant);
        return new Message(tenantId + " saved successfully!");
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public Message assignRole(String email, String tenantId, String role) throws ProductNotFoundException {
        TenantContext.setCurrentProfile("profileshop");
        Customer customer = customerService.findByEmail(email);
        TenantAuthority authority = new TenantAuthority();
        authority.setTenantId(tenantId);
        authority.setUserId(customer.getId());
        authority.setAuthority(role);
        tenantAuthorityRepository.save(authority);
        return new Message(tenantId + ": " + role + " saved successfully!");

    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @Transactional
    public Message addResponse(AddSignUpResponseInput input) {
        TenantContext.setCurrentProfile("profileshop");
        preOnBoardingService.saveSignUpResponse(input);
        return new Message("Response added successfully!");
    }

}

