package com.badals.shop.aop.tenant;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;

@Aspect
@Component
public class TenantServiceAspect {
   @AfterReturning(
           pointcut="bean(entityManagerFactory) && execution(* createEntityManager(..))",
           returning="retVal")
   public void getSessionAfter(JoinPoint joinPoint, Object retVal) {
      if (retVal != null && EntityManager.class.isInstance(retVal) && TenantContext.getCurrentTenant() != null) {
         Session session = ((EntityManager) retVal).unwrap(Session.class);
         session.enableFilter("tenantFilter").setParameter("tenantId", TenantContext.getCurrentTenant());
      }
   }
}
