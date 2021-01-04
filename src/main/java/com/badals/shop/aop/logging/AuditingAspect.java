package com.badals.shop.aop.logging;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.web.bind.ServletRequestDataBinder;

import java.sql.Timestamp;

@Aspect
@Component
public class AuditingAspect {

/*   private final TenantRepository tenantRepository;

   @Autowired
   public TenantAspect(TenantRepository tenantRepository) {
      this.tenantRepository = tenantRepository;
   }*/

    @Around("execution(* com.badals.shop.service.mutation.*.*(..))")
    public Object beforeWebMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
        // only log those methods called by an end user
        if(principal.getUsername() != null)
            System.out.println(principal.getUsername());

        System.out.println(methodName);
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        int i = 0;
        for(Object o : args) {

            System.out.println("First parameter's name: " + codeSignature.getParameterNames()[i]);
            System.out.println("First argument's value: " + joinPoint.getArgs()[i++]);
        }

        return joinPoint.proceed();
    }
}
