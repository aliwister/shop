package com.badals.shop.aop.tenant;

import com.badals.shop.domain.User;
import com.badals.shop.repository.UserRepository;
import com.badals.shop.security.SecurityUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

@Aspect
@Component
public class TenantAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final String fieldName =  "tenantId";

    private final Logger log = LoggerFactory.getLogger(TenantAspect.class);

    /**
     * Run method if User service is hit.
     * Filter users based on which tenant the user is associated with.
     * Skip filter if user has no tenant
     */
    @Before("execution(* com.badals.shop.service.CustomerService.*(..)) || execution(* com.badals.shop.security.DomainCustomerDetailsService.*(..))")
    public void beforeExecution() throws Throwable {
       //if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("TENANT_FILTER");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		//}
    }

    @Before("execution(* com.badals.shop.service.TenantProductService.*(..)) || execution(* com.badals.shop.service.TenantCartService.*(..))")
    public void beforeGraphQLExecution() throws Throwable {
       if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		}
    }
}
