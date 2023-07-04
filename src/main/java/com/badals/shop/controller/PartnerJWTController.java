package com.badals.shop.controller;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.security.SecurityUtils;
import com.badals.shop.security.jwt.ProfileUser;
import com.badals.shop.web.rest.errors.InvalidPasswordException;
import com.badals.shop.web.rest.vm.LoginVM;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.repository.UserRepository;
import com.badals.shop.security.jwt.JWTFilter;
import com.badals.shop.security.jwt.TokenProvider;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class PartnerJWTController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;

    public PartnerJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, TenantRepository tenantRepository, UserRepository userRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
    }
    @GetMapping("/partner-me")
    public ResponseEntity<PartnerJWTController.JwtPartnerAuthenticationResponse> getUserWithAuthorities() throws IllegalAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object userObj = authentication.getPrincipal();

        if (userObj == null|| userObj.equals("anonymousUser")) {
            throw new IllegalAccessException("Not Authorized");
        }
        ProfileUser user = (ProfileUser) userObj;
        com.badals.shop.domain.User dbUser = userRepository.findOneByEmailIgnoreCaseAndTenantId(user.getUsername(), com.badals.shop.domain.User.tenantFilter).get();
        HttpHeaders httpHeaders = new HttpHeaders();
        List<Tenant> tenantList = tenantRepository.findTenantsForUser(dbUser.getId());
        return new ResponseEntity<>(new PartnerJWTController.JwtPartnerAuthenticationResponse(null, authentication.getPrincipal(), tenantList, user.isStoreSelect()), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/store-select")
    public ResponseEntity<JwtAuthenticationResponse> authorize(@Valid @RequestBody Attribute tenantAttribute) throws IllegalAccessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object userObj = authentication.getPrincipal();

        if (userObj == null|| userObj.equals("anonymousUser")) {
            throw new IllegalAccessException("Not Authorized");
        }
        User user = (User) userObj;
        String tenantId = tenantAttribute.getValue();
        com.badals.shop.domain.User dbUser = userRepository.findOneByEmailIgnoreCaseAndTenantId(user.getUsername(), com.badals.shop.domain.User.tenantFilter).get();
        //Tenant tenant = tenantRepository.getIsTenantForCustomer(user.getUsername(), tenantId).orElse(null);


        List<GrantedAuthority> grantedAuthorities = tenantRepository.getTenantAuthorities(dbUser.getId(), tenantId).stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                .collect(Collectors.toList());

        if (grantedAuthorities.isEmpty())
            throw new IllegalAccessException("Not Authorized");

        LocaleContextHolder.getLocale();
        boolean rememberMe = true;
        String jwt = tokenProvider.createPartnerToken(authentication, rememberMe, tenantId, grantedAuthorities);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtAuthenticationResponse(jwt, user, grantedAuthorities), httpHeaders, HttpStatus.OK);
    }

    // This loads the authorities of a user for a particular tenant
    @PostMapping("/authenticate2")
    public ResponseEntity<PartnerJWTController.JwtPartnerAuthenticationResponse> authorizePartner(@Valid @RequestBody LoginVM loginVM) {
        LocaleContextHolder.getLocale();
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        com.badals.shop.domain.User dbUser = userRepository.findOneByEmailIgnoreCaseAndTenantId(loginVM.getUsername(), com.badals.shop.domain.User.tenantFilter).get();
        List<Tenant> tenantList = tenantRepository.findTenantsForUser(dbUser.getId());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new PartnerJWTController.JwtPartnerAuthenticationResponse(jwt, authentication.getPrincipal(), tenantList, true), httpHeaders, HttpStatus.OK);
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }

    @Data
    static class JwtAuthenticationResponse {
        private final String authorities;
        @JsonProperty("id_token")
        private final String idToken;
        private final String tokenType = "Bearer";
        private final String username;

        public JwtAuthenticationResponse(String accessToken, Object user, List<GrantedAuthority> grantedAuthorities) {
            this.idToken = accessToken;
            User userP = (User) user;
            //this.id = userP.
            this.username = userP.getUsername();
            //this.firstName = userP.getName();
            this.authorities = String.join(",",grantedAuthorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        }
    }

    @Data
    static class JwtPartnerAuthenticationResponse {
        private final String authorities;
        private final String tenants;
        @JsonProperty("id_token")
        private final String idToken;
        private final Boolean isStoreSelect;
        private final String tokenType = "Bearer";
        private final String username;

        public JwtPartnerAuthenticationResponse(String accessToken, Object user, List<Tenant> stores, Boolean isStoreSelect) {
            this.idToken = accessToken;
            User userP = (User) user;
            //this.id = userP.
            this.username = userP.getUsername();
            this.isStoreSelect = isStoreSelect;
            //this.firstName = userP.getName();
            this.authorities = String.join(",",userP.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
            this.tenants = String.join(",",stores.stream().map(Tenant::getTenantId).collect(Collectors.toSet()));
        }
    }
}
