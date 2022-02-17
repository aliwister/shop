package com.badals.shop.config;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.aop.tenant.TenantSupport;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

import javax.sql.DataSource;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class TenantInterceptor {

   @Autowired
   private JpaProperties jpaProperties;

   @Bean
   public EmptyInterceptor hibernateInterceptor() {
      return new EmptyInterceptor() {

         @Override
         public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
            if (entity instanceof TenantSupport) {
               ((TenantSupport) entity).setTenantId(TenantContext.getCurrentProfile());
            }
         }


         @Override
         public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
            if (entity instanceof TenantSupport) {
               ((TenantSupport) entity).setTenantId(TenantContext.getCurrentProfile());
               return true;
            }
            return false;
         }

         @Override
         public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
            if (entity instanceof TenantSupport) {
               //((TenantSupport) entity).setTenantId(TenantContext.getCurrentProfile());
               //return super.onSave(entity, id, state, propertyNames, types);
               for ( int i=0; i<propertyNames.length; i++ ) {
                  if ( "tenantId".equals( propertyNames[i] ) ) {
                     state[i] = TenantContext.getCurrentProfile();
                     return true;
                  }
               }
            }
            return false;
         }
      };
   }

   @Bean
   public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder factory, DataSource dataSource, JpaProperties properties) {
      Map<String, Object> jpaPropertiesMap = new HashMap<>(jpaProperties.getProperties());
      jpaPropertiesMap.put("hibernate.ejb.interceptor", hibernateInterceptor());
      return factory.dataSource(dataSource).packages("com.badals.shop.domain").properties(jpaPropertiesMap).build();
   }
}

