package com.badals.shop.aop.logging;

import com.badals.shop.domain.Merchant;
import com.badals.shop.domain.Tenant;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.repository.TenantRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Component
public class TenantAspect {


   private final TenantRepository tenantRepository;

   @Autowired
   public TenantAspect(TenantRepository tenantRepository) {
      this.tenantRepository = tenantRepository;
   }

   @Around(value = "execution(* com.badals.shop.graph.mutation.MerchantMutation.*(..)) || execution(* com.badals.shop.graph.query.MerchantQuery.*(..)) || " +
           "execution(* com.badals.shop.graph.mutation.PartnerMutation.*(..)) || execution(* com.badals.shop.graph.query.PartnerQuery.*(..))")
   public Object assignForController(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      return assignTenant(proceedingJoinPoint);
   }

   private Object assignTenant(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
      try {
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         if (user == null) {
            throw new IllegalAccessException("Not Authorized");
         }
         List<Tenant> tenantList = tenantRepository.findTenantAndMerchantByCustomer(user.getUsername());
         if (tenantList.size() > 0) {
            Tenant t = tenantList.get(0);
            TenantContext.setCurrentTenant(t.getName());
            TenantContext.setCurrentTenantId(t.getId());
            List<Merchant> merchants = t.getMerchants();
            if (merchants.size() > 0) {
               TenantContext.setCurrentMerchant(merchants.get(0).getName());
               TenantContext.setCurrentMerchantId(merchants.get(0).getId());
            }
         }
      } finally {
         Object retVal;
         retVal = proceedingJoinPoint.proceed();
         TenantContext.clear();
         return retVal;
      }
   }
}
