package com.badals.shop.aop.logging;

import com.badals.shop.domain.Action;
import com.badals.shop.repository.ActionRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.CodeSignature;
import org.elasticsearch.action.ActionRequestValidationException;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ActionRepository actionRepository;

    @Around("execution(* com.badals.shop.service.mutation.*.*(..))")
    public Object beforeWebMethodExecution(ProceedingJoinPoint joinPoint) throws Throwable {

        Object[] args = joinPoint.getArgs();
        String methodName = joinPoint.getSignature().getName();
        User principal = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String className = joinPoint.getSignature().getDeclaringTypeName();
        Timestamp timestamp = new Timestamp(new java.util.Date().getTime());
        // only log those methods called by an end user
        if(principal.getUsername() != null)
            System.out.println(principal.getUsername());

        System.out.println(methodName);
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        int i = 0;
        Action action = new Action();
        action.setAction(methodName);
        action.setObject(getObject(className));

        String state = "";
        for(Object o : args) {
            String key, value;
            key = codeSignature.getParameterNames()[i];
            value = joinPoint == null || joinPoint.getArgs() == null || joinPoint.getArgs()[i] == null?"":joinPoint.getArgs()[i].toString();
            System.out.println("First parameter's name: " + key);
            System.out.println("First argument's value: " + value);

/*            if (key.equalsIgnoreCase("status") || key.equalsIgnoreCase("state"))
                action.setState(value);*/
            if (key.equalsIgnoreCase("comment"))
                action.setComment(value);
            if (key.toLowerCase().endsWith("id"))
                action.setObjectId(value);
            else
                state += " "+key+ "="+value;

            i++;
        }
        action.setState(state);
        actionRepository.save(action);
        return joinPoint.proceed();
    }

    private String getObject(String className) {
        if(className.equalsIgnoreCase("AdminMutation"))
            return "order";
        if(className.equalsIgnoreCase("OrderMutation"))
            return "order";
        if(className.equalsIgnoreCase("PaymentMutation"))
            return "payment";
        if(className.equalsIgnoreCase("ShipmentMutation"))
            return "shipment";
        if(className.equalsIgnoreCase("PurchaseMutation"))
            return "purchase";

        return "order";
    }
}
