package com.badals.shop.aop.tenant;

import com.badals.shop.domain.Merchant;
import com.badals.shop.domain.Tenant;
import com.badals.shop.repository.TenantRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

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
      Object userObj =  SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      if (userObj == null|| userObj.equals("anonymousUser")) {
         return assignTenant(proceedingJoinPoint, "mayaseen");
         //throw new IllegalAccessException("Not Authorized");
      }
      User user = (User) userObj;
      List<Tenant> tenantList = tenantRepository.findTenantAndMerchantByCustomer(user.getUsername());
      String tenant = null;
      if (tenantList.size() > 0) {
         Tenant t = tenantList.get(0);
         tenant = t.getTenantId();
         //TenantContext.setCurrentTenantId(t.getId());
/*         List<Merchant> merchants = t.getMerchants();
         if (merchants.size() > 0) {
            TenantContext.setCurrentMerchant(merchants.get(0).getName());
            //TenantContext.setCurrentMerchantId(merchants.get(0).getId());
         }*/
      }

      if (tenant != null) {
         Filter filter = entityManager.unwrap(Session.class).enableFilter("TENANT_FILTER");
         filter.setParameter("tenantId", tenant);
         filter.validate();
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
