package com.badals.shop.aop.tenant;

import com.badals.shop.repository.UserRepository;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class TenantTrustAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final String fieldName =  "tenantId";

    private final Logger log = LoggerFactory.getLogger(TenantTrustAspect.class);

   /**
    * Run method for open services.
    * Sets a filter to the tenant Id which is captured using X-TenantId or graphql/[tenant_id]
    * Picks records corresponding to the filter
    */
    @Before("execution(* com.badals.shop.service.PurchaseService.*(..))")
    public void beforeGraphQLExecution() throws Throwable {
       if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		}
    }

}
