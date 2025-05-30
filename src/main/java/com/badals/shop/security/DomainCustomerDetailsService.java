package com.badals.shop.security;

import com.badals.shop.domain.Customer;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("customerDetailsService")
public class DomainCustomerDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainCustomerDetailsService.class);

    private final CustomerRepository userRepository;

    public DomainCustomerDetailsService(CustomerRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        //if (new EmailValidator().isValid(login, null)) {
            return userRepository.findOneWithAuthoritiesByEmail(login)
                .map(user -> createSpringSecurityUser(login, user))
                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
       // }

        /*String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin)
            .map(user -> createSpringSecurityUser(lowercaseLogin, user))
            .orElseThrow(() -> new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database"));
        */
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, Customer user) {
/*        if (user.getActive() == 0) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }*/
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
            .map(authority -> new SimpleGrantedAuthority(authority.getName()))
            .collect(Collectors.toList());

        //Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
        //grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            grantedAuthorities);
    }
}
