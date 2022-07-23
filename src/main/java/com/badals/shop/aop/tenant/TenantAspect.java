package com.badals.shop.aop.tenant;

import com.badals.shop.repository.UserRepository;
import com.badals.shop.security.SecurityUtils;
import com.badals.shop.security.jwt.ProfileUser;
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
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		//}
    }


   /**
    * Run method for open services.
    * Sets a filter to the tenant Id which is captured using X-TenantId or graphql/[tenant_id]
    * Picks records corresponding to the filter
    */
    @Before("execution(* com.badals.shop.service.TenantProductService.*(..)) || execution(* com.badals.shop.service.TenantCartService.*(..))|| execution(* com.badals.shop.service.TenantLayoutService.*(..)) ")
    public void beforeGraphQLExecution() throws Throwable {
       if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		}
    }

    @Before("execution(* com.badals.shop.service.TenantOrderService.*(..)) ")
    public void beforeGraphQLExecution3() throws Throwable {
       if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		}
    }

    @Before("execution(* com.badals.shop.service.TenantAccountService.*(..))")
    public void beforeGraphQLExecution2() throws Throwable {
       if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		}
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       User userObj =  (User) authentication.getPrincipal();
       if(userObj != null && userObj.getUsername() != null) {
          Filter filter = entityManager.unwrap(Session.class).enableFilter("userFilter");
          filter.setParameter("username", userObj.getUsername());
          filter.validate();
       }
    }
}
