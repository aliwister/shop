package com.badals.shop.web.rest;


import com.badals.shop.domain.Customer;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.repository.UserRepository;
import com.badals.shop.security.jwt.JWTFilter;
import com.badals.shop.security.jwt.TokenProvider;
import com.badals.shop.service.CustomerService;
import com.badals.shop.service.util.MailService;
import com.badals.shop.service.dto.PasswordChangeDTO;
import com.badals.shop.service.dto.UserDTO;
import com.badals.shop.web.rest.errors.*;
import com.badals.shop.web.rest.vm.KeyAndPasswordVM;
import com.badals.shop.web.rest.vm.ManagedUserVM;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class UserCustomerAccountResource {

    private static class AccountResourceException extends RuntimeException {
        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final Logger log = LoggerFactory.getLogger(UserCustomerAccountResource.class);

    private final CustomerRepository userRepository;
    private final UserRepository user2Repository;
    private final CustomerService userService;
    private final TenantRepository tenantRepository;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final MailService mailService;

    public UserCustomerAccountResource(CustomerRepository userRepository, CustomerService userService, MailService mailService, UserRepository user2Repository, TenantRepository tenantRepository, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {

        this.userRepository = userRepository;
        this.userService = userService;
        this.mailService = mailService;
        this.user2Repository = user2Repository;
        this.tenantRepository = tenantRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    /**
     * {@code POST  /register} : register the user.
     *
     * @param managedUserVM the managed user View Model.
     * @return
     * @throws InvalidPasswordException  {@code 400 (Bad Request)} if the password is incorrect.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already used.
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<JwtPartnerAuthenticationResponse> registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        Customer user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(user.getEmail(), managedUserVM.getPassword());
        com.badals.shop.domain.User dbUser = user2Repository.findOneByEmailIgnoreCaseAndTenantId(user.getEmail(), com.badals.shop.domain.User.tenantFilter).get();
        List<Tenant> tenantList = tenantRepository.findTenantsForUser(dbUser.getId());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = true;
        String jwt = tokenProvider.createToken(authentication, rememberMe, true);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        mailService.sendActivationEmail(user);
        return new ResponseEntity<>(new UserCustomerAccountResource.JwtPartnerAuthenticationResponse(jwt, authentication.getPrincipal(), tenantList, true), httpHeaders, HttpStatus.OK);

    }

    /**
     * {@code GET  /activate} : activate the registered user.
     *
     * @param key the activation key.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be activated.
     */
    @GetMapping("/activate")
    public void activateAccount(@RequestParam(value = "key") String key, @RequestParam(value = "email") String email) {
        Optional<Customer> user = userService.activateRegistration(key, email);
        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this activation key");
        }
    }

    /**
     * {@code GET  /authenticate} : check if the user is authenticated, and return its login.
     *
     * @param request the HTTP request.
     * @return the login if the user is authenticated.
     */
    @GetMapping("/authenticate")
    public String isAuthenticated(HttpServletRequest request) {
        log.debug("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * {@code GET  /account} : get the current user.
     *
     * @return the current user.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user couldn't be returned.
     */
    @GetMapping("/account")
    public UserDTO getAccount() {
        return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    /**
     * {@code POST  /account} : update the current user information.
     *
     * @param userDTO the current user information.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already used.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the user login wasn't found.
     */
    @PostMapping("/account")
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        /*String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new AccountResourceException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new AccountResourceException("User could not be found");
        }
        //userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(),
        //    userDTO.getLangKey(), userDTO.getImageUrl());
    */}

    /**
     * {@code POST  /account/change-password} : changes the current user's password.
     *
     * @param passwordChangeDto current and new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the new password is incorrect.
     */
    @PostMapping(path = "/account/change-password")
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        //userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * {@code POST   /account/reset-password/init} : Send an email to reset the password of the user.
     *
     * @param mail the mail of the user.
     * @throws EmailNotFoundException {@code 400 (Bad Request)} if the email address is not registered.
     */
    @PostMapping(path = "/account/reset-password/init")
    public void requestPasswordReset(@RequestBody String mail) {
       mailService.sendPasswordResetMail(
           userService.requestPasswordReset(mail)
               .orElseThrow(EmailNotFoundException::new)
       );
    }

    /**
     * {@code POST   /account/reset-password/finish} : Finish to reset the password of the user.
     *
     * @param keyAndPassword the generated key and the new password.
     * @throws InvalidPasswordException {@code 400 (Bad Request)} if the password is incorrect.
     * @throws RuntimeException {@code 500 (Internal Server Error)} if the password could not be reset.
     */
    @PostMapping(path = "/account/reset-password/finish")
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<Customer> user =
            userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new AccountResourceException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) &&
            password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH &&
            password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
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
