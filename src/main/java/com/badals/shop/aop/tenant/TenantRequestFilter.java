package com.badals.shop.aop.tenant;

import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class TenantRequestFilter extends OncePerRequestFilter {

   private final Logger log = LoggerFactory.getLogger(TenantRequestFilter.class);

   @Autowired
   private TenantRepository tenantRepository;

   /**
    * Capture the X-TenantID for rest requests
    * Capture the tenantId from the graphql URL for GraphQL requests
    * @param req
    * @param res
    * @param chain
    * @throws ServletException
    * @throws IOException
    */

   @Override
   protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
      String name = req.getRequestURI().startsWith("/graphql/")?req.getRequestURI().substring(9):null;
      Long profileId = null;

      if(name != null) {
         Tenant tenant = tenantRepository.findByTenantId(name).orElse(null);
         if(tenant != null) {
            profileId = tenant.getId();
         }
         TenantContext.setCurrentProfile(name);
         TenantContext.setCurrentProfileId(profileId);
         chain.doFilter(req, res);
         return;
      }

      name = req.getHeader("X-TenantID");
      TenantContext.setCurrentProfile(name);
      TenantContext.setCurrentProfileId(profileId);

      chain.doFilter(req, res);
   }
}