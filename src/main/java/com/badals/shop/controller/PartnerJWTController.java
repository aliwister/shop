package com.badals.shop.controller;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.security.SecurityUtils;
import com.badals.shop.security.jwt.ProfileUser;
import com.badals.shop.service.CustomerService;
import com.badals.shop.service.util.RandomUtil;
import com.badals.shop.web.rest.errors.InvalidPasswordException;
import com.badals.shop.web.rest.vm.GoogleLoginVM;
import com.badals.shop.web.rest.vm.LoginVM;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class PartnerJWTController {

    private final Logger logger =  LoggerFactory.getLogger(PartnerJWTController.class);
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TenantRepository tenantRepository;
    private final UserRepository userRepository;
    private final GoogleIdTokenVerifier googleIdTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singletonList("333813192941-5qd2vfdfif0000q1ehu13tkorhtqmqfp.apps.googleusercontent.com"))
        .build();
    private final CustomerRepository customerRepository;


    public PartnerJWTController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder, TenantRepository tenantRepository, UserRepository userRepository, CustomerRepository customerRepository) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.tenantRepository = tenantRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
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

    // New method to handle Google Sign-In
    @PostMapping("/google_signIn")
    public ResponseEntity<?> signInWithGoogle(@RequestBody GoogleLoginVM body) {
        String idToken = body.getIdToken();
        logger.info(googleIdTokenVerifier.toString());
        try {
            logger.info("got idToken : " + idToken);
            GoogleIdToken googleIdToken = googleIdTokenVerifier.verify(idToken);
            if (googleIdToken != null) {
                GoogleIdToken.Payload payload = googleIdToken.getPayload();

                // Get user information from Google token payload
                String email = payload.getEmail();
                logger.info("got email : " + email);
                Customer user = customerRepository.findOneByEmailIgnoreCaseAndTenantId(email, com.badals.shop.domain.User.tenantFilter).orElse(null);
                if (user == null) {
                    String name = (String) payload.get("name");
                    String familyName = (String) payload.get("family_name");

                    user = new Customer();
                    user.setEmail(email);
                    user.setFirstname(name);
                    user.setLastname(familyName);
                    user.setActive(true);
                    user.setSecureKey(RandomUtil.generateActivationKey());

                    // Set other user properties as needed
                    user = customerRepository.save(user);
                }

                UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(email, null);
                List<Tenant> tenantList = tenantRepository.findTenantsForUser(user.getId());
                Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);

                // Generate JWT token
                boolean rememberMe = true; // You might have your own logic for rememberMe
                String jwt = tokenProvider.createToken(authentication, rememberMe, true);

                // Return JWT token or any necessary response
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
                return new ResponseEntity<>(new PartnerJWTController.JwtPartnerAuthenticationResponse(jwt, authentication.getPrincipal(), tenantList, true), httpHeaders, HttpStatus.OK);
            }
        } catch (GeneralSecurityException | IOException e) {
            logger.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }

        // Return error response if token verification fails
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token verification failed");
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
