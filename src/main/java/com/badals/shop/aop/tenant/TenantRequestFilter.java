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

   @Override
   protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws ServletException, IOException {
      String name = req.getRequestURI().startsWith("/graphql/")?req.getRequestURI().substring(9):null;
      Long profileId = null;

      if(name != null) {
         Tenant tenant = tenantRepository.findByNameIgnoreCase(name).orElse(null);
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

/*      Cookie[] cookies = req.getCookies();
      //log.info("---------------------COOOOOOOOOOKIIIIIIIIIIIIIIIIIIIIIIEEEEEEEEEEEEEEE");
      //log.info(((HttpServletRequest) request).getRequestURI());
      if (cookies != null) {
         for (Cookie ck : cookies) {
           *//* if ("nameOfMyCookie".equals(ck.getName())) {
               // read the cookie etc, etc
               // ....
               // set an object in the current request
               request.setAttribute("myCoolObject", myObject)
            }*//*
            //log.info(ck.getName() + " " + ck.getValue());
         }
      }
      Cookie c = new Cookie("_temp", "_temp");
      c.setPath("/");
      c.setMaxAge(-1);
      res.addCookie(c);*/
      chain.doFilter(req, res);

   }

   // other methods
}