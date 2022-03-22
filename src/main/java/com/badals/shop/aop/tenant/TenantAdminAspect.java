package com.badals.shop.aop.tenant;

import com.badals.shop.repository.TenantRepository;
import com.badals.shop.security.jwt.ProfileUser;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Aspect
@Component
public class TenantAdminAspect {
   @PersistenceContext
   private EntityManager entityManager;

   private final TenantRepository tenantRepository;

   @Autowired
   public TenantAdminAspect(TenantRepository tenantRepository) {
      this.tenantRepository = tenantRepository;
   }

   @Around(value = "execution(* com.badals.shop.service.TenantAdminProductService.*(..)) || execution(* com.badals.shop.service.TenantAdminOrderService.*(..)) .*(..))")
   public Object assignForController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      ProfileUser userObj =  (ProfileUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (userObj == null|| userObj.equals("anonymousUser")) {
         //return assignTenant(proceedingJoinPoint, "mayaseen");
         throw new IllegalAccessException("Not Authorized");
      }
      //User user = (User) userObj;
      //List<Tenant> tenantList = tenantRepository.findTenantAndMerchantByCustomer(user.getUsername());
      String tenant = userObj.getTenantId();

      if (tenant != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("TENANT_FILTER");
         filter.setParameter("tenantId", tenant);
         filter.validate();
      } else {
         throw new IllegalAccessException("Not Authorized");
      }
      return assignTenant(proceedingJoinPoint, tenant);
   }

   private Object assignTenant(ProceedingJoinPoint proceedingJoinPoint, String tenant) throws Throwable {
      try {
         TenantContext.setCurrentTenant(tenant);
         TenantContext.setCurrentProfile(tenant);
      } finally {
         Object retVal;
         retVal = proceedingJoinPoint.proceed();
         TenantContext.clear();
         return retVal;
      }
   }
}
