package com.badals.shop.controller;

import com.badals.shop.repository.TenantRepository;
import com.badals.shop.security.jwt.JWTFilter;
import com.badals.shop.security.jwt.TokenProvider;
import com.badals.shop.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final TokenProvider tokenProvider;
    private final TenantRepository tenantRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public UserJWTController(TokenProvider tokenProvider, TenantRepository tenantRepository, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.tenantRepository = tenantRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    // This authenticates customers of a given tenant
    @PostMapping("/authenticate")
    public ResponseEntity<JwtAuthenticationResponse> authorize(@Valid @RequestBody LoginVM loginVM) {
        LocaleContextHolder.getLocale();
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtAuthenticationResponse(jwt, authentication.getPrincipal()), httpHeaders, HttpStatus.OK);
    }


    // This loads the authenticates a tenant/user. Tenant user can have accesst to multiple tenant admin side.
/*    @PostMapping("/authenticateUser")
    public ResponseEntity<JwtPartnerAuthenticationResponse> authorizeUser(@Valid @RequestBody LoginVM loginVM) {
        LocaleContextHolder.getLocale();
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        List<Tenant> tenantList = tenantRepository.findTenantAndMerchantByCustomer(loginVM.getUsername());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.getRememberMe() == null) ? false : loginVM.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtPartnerAuthenticationResponse(jwt, authentication.getPrincipal(), tenantList), httpHeaders, HttpStatus.OK);
    }*/



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

        public JwtAuthenticationResponse(String accessToken, Object user) {
            this.idToken = accessToken;
            User userP = (User) user;
            //this.id = userP.
            this.username = userP.getUsername();
            //this.firstName = userP.getName();
            this.authorities = String.join(",",userP.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()));
        }
    }


}
