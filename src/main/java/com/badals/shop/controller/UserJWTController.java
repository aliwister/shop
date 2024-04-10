package com.badals.shop.controller;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.security.DomainCustomerDetailsService;
import com.badals.shop.security.jwt.JWTFilter;
import com.badals.shop.security.jwt.TokenProvider;
import com.badals.shop.service.CustomerService;
import com.badals.shop.service.dto.UserDTO;
import com.badals.shop.web.rest.vm.GoogleLoginVM;
import com.badals.shop.web.rest.vm.LoginVM;

import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final Logger logger =  LoggerFactory.getLogger(UserJWTController.class);

    private final TokenProvider tokenProvider;
    private final TenantRepository tenantRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singletonList("333813192941-5qd2vfdfif0000q1ehu13tkorhtqmqfp.apps.googleusercontent.com"))
        .build();
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;
    private final DomainCustomerDetailsService domainUserDetailsService;

    public UserJWTController(TokenProvider tokenProvider, TenantRepository tenantRepository, AuthenticationManagerBuilder authenticationManagerBuilder, CustomerRepository customerRepository, CustomerService customerService, DomainCustomerDetailsService domainUserDetailsService) {
        this.tokenProvider = tokenProvider;
        this.tenantRepository = tenantRepository;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
        this.domainUserDetailsService = domainUserDetailsService;
    }

    // This authenticates customers of a given tenant
    @PostMapping("/authenticate")
    public ResponseEntity<JwtAuthenticationResponse> authorize(@Valid @RequestBody LoginVM loginVM) {
        LocaleContextHolder.getLocale();
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginVM.getUsername(), loginVM.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = loginVM.getRememberMe() != null && loginVM.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JwtAuthenticationResponse(jwt, authentication.getPrincipal()), httpHeaders, HttpStatus.OK);
    }

    @PostMapping("/google_signIn2")
    public ResponseEntity<?> signInWithGoogle(@RequestBody GoogleLoginVM body) {
        String idToken = body.getIdToken();
        try {
            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();

                String email = payload.getEmail();
                Customer user = customerRepository.findOneByEmailIgnoreCaseAndTenantId(email, com.badals.shop.domain.User.tenantFilter).orElse(null);
                if (user == null) {
                    UserDTO userDTO = new UserDTO();
                    userDTO.setEmail(email);
                    userDTO.setFirstName((String) payload.get("name"));
                    userDTO.setLastName((String) payload.get("family_name"));
                    userDTO.setActivated(false);
                    customerService.registerUser(userDTO, "eksNKAeu&EyL#5nK$sj&z$QuRv$huHbE8gH3$tnowtfb%Xb&%yBz&5*2JmWCxsn@^M3%NCXH*Df$2#!#8A%jFnDMuAdx6tamqxPpn6RuSrN9hz5@EiuCX");
                }

                UserDetails userDetails = domainUserDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails,"null");

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

                String jwt = tokenProvider.createToken(authenticationToken, true);

                // Return JWT token or any necessary response
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
                return new ResponseEntity<>(new JwtAuthenticationResponse(jwt, authenticationToken.getPrincipal()), httpHeaders, HttpStatus.OK);
            }
        } catch (GeneralSecurityException | IOException e) {
            logger.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getStackTrace());
        }

        // Return error response if token verification fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed");
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
