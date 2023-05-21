package com.badals.shop.aop.tenant;

import com.badals.shop.repository.UserRepository;
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
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class TenantUserAspect {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    private final String fieldName =  "tenantId";

    private final Logger log = LoggerFactory.getLogger(TenantUserAspect.class);



    @Before("execution(* com.badals.shop.service.TenantAdminOrderService.*(..)) ")
    public void beforeGraphQLExecution3() throws Throwable {
       if (TenantContext.getCurrentProfile() != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
         filter.setParameter("tenantId", TenantContext.getCurrentProfile());
         filter.validate();
		}
       ProfileUser userObj =  (ProfileUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
       if(userObj != null && userObj.getUsername() != null ) {
          if (!userObj.hasAuthority(SecurityContextHolder.getContext().getAuthentication(), "ROLE_ADMIN")) {
             Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantUserFilter");
             filter.setParameter("createdBy", userObj.getUsername());
             filter.validate();
          }
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
