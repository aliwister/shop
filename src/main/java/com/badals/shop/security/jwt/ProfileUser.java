package com.badals.shop.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.util.Collection;

public class ProfileUser extends User {
   public ProfileUser(String username, String password, Collection<? extends GrantedAuthority> authorities) {
      super(username, password, authorities);
   }

   public ProfileUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
      super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
   }


   // Used for backoffice
   @Getter
   @Setter
   String tenantId;

   // Used for front-end
   @Getter
   @Setter
   String profile;

   public boolean hasAuthority(Authentication authentication, String authority) {
      return authentication.getAuthorities().stream()
              .map(GrantedAuthority::getAuthority)
              .anyMatch(authority::equals);
   }
}
