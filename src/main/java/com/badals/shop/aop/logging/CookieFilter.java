package com.badals.shop.aop.logging;

import com.badals.shop.domain.Tenant;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CookieFilter implements Filter {

   private final Logger log = LoggerFactory.getLogger(CookieFilter.class);

   @Autowired
   private TenantRepository tenantRepository;

   @Override
   public void init(FilterConfig filterConfig) throws ServletException {

   }

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

      HttpServletRequest req = (HttpServletRequest) request;


      String name = req.getRequestURI().startsWith("/graphql/")?req.getRequestURI().substring(9):null;
      Long profileId = null;

      if(name != null) {
         Tenant tenant = tenantRepository.findByNameIgnoreCase(name).orElse(null);
         if(tenant != null) {
            profileId = tenant.getId();
         }
         TenantContext.setCurrentProfile(name);
         TenantContext.setCurrentProfileId(profileId);
      }



      Cookie[] cookies = req.getCookies();
      //log.info("---------------------COOOOOOOOOOKIIIIIIIIIIIIIIIIIIIIIIEEEEEEEEEEEEEEE");
      //log.info(((HttpServletRequest) request).getRequestURI());
      if (cookies != null) {
         for (Cookie ck : cookies) {
           /* if ("nameOfMyCookie".equals(ck.getName())) {
               // read the cookie etc, etc
               // ....
               // set an object in the current request
               request.setAttribute("myCoolObject", myObject)
            }*/
            //log.info(ck.getName() + " " + ck.getValue());
         }
      }
      HttpServletResponse res = (HttpServletResponse) response;
      Cookie c = new Cookie("_temp", "_temp");
      c.setPath("/");
      c.setMaxAge(-1);
      res.addCookie(c);
      chain.doFilter(request, response);

   }

   @Override
   public void destroy() {

   }

   // other methods
}