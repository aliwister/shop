package com.badals.shop.aop.logging;

import com.badals.shop.aop.tenant.TenantContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LocaleAspect {

    @Around("execution(* com.badals.shop.graph.query.ProductQuery.product(..)) || "+
            "execution(* com.badals.shop.graph.query.ProductQuery.getProductBySku(..)) || " +
            //"execution(* com.badals.shop.graph.query.TenantQuery.*(..)) || " +
            //"execution(* com.badals.shop.graph.mutation.TenantMutation.*(..)) || " +
            "execution(* com.badals.shop.*.product(..)))")
    public Object beforeWebMethodExecution1(ProceedingJoinPoint joinPoint) throws Throwable {
        setLocale(joinPoint);
        return joinPoint.proceed();
    }

    private Object setLocale(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();

        int i = 0;
        LocaleContext.setLocale("en");
        try {
            String state = "";
            for(Object o : args) {
                String key, value;
                key = codeSignature.getParameterNames()[i];
                value = joinPoint == null || joinPoint.getArgs() == null || joinPoint.getArgs()[i] == null?"":joinPoint.getArgs()[i].toString();
/*                System.out.println("First parameter's name: " + key);
                System.out.println("First argument's value: " + value);*/

                if (key.equalsIgnoreCase("_locale")) {
                    LocaleContext.setLocale(value);
                    break;
                }

                i++;
            }
        } finally {
            Object retVal;
            retVal = joinPoint.proceed();
            TenantContext.clear();
            return retVal;
        }
    }

}
