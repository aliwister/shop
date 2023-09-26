package com.badals.shop.service;

import com.badals.shop.domain.Authority;
import com.badals.shop.domain.Customer;
import com.badals.shop.repository.AuthorityRepository;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.security.AuthoritiesConstants;
import com.badals.shop.security.SecurityUtils;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.dto.UserDTO;
import com.badals.shop.service.mapper.CustomerMapper;
import com.badals.shop.service.util.RandomUtil;
import com.badals.shop.web.rest.errors.EmailAlreadyUsedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Customer}.
 */
@Service
@Transactional
public class CustomerService {

    private final Logger log = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    private final PasswordEncoder passwordEncoder;

    private final AuthorityRepository authorityRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper, PasswordEncoder passwordEncoder, AuthorityRepository authorityRepository) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.passwordEncoder = passwordEncoder;
        this.authorityRepository = authorityRepository;
    }

    /**
     * Save a customer.
     *
     * @param customerDTO the entity to save.
     * @return the persisted entity.
     */
    public CustomerDTO save(CustomerDTO customerDTO) {
        log.debug("Request to save Customer : {}", customerDTO);
        Customer customer = customerMapper.toEntity(customerDTO);
        customer = customerRepository.save(customer);
        return customerMapper.toDto(customer);
    }

    /**
     * Get all the customers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerDTO> findAll() {
        log.debug("Request to get all Customers");
        return customerRepository.findAll().stream()
            .map(customerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one customer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CustomerDTO> findOne(Long id) {
        log.debug("Request to get Customer : {}", id);
        return customerRepository.findById(id)
            .map(customerMapper::toDto);
    }

    /**
     * Delete the customer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Customer : {}", id);
        customerRepository.deleteById(id);
    }

    public Customer findByEmail(String email) {
        return customerRepository.findByEmailIgnoreCase(email).orElse(null);
    }

    public CustomerDTO findByMobileJoinAddresses(String mobile) {
        List<Customer> customerList = customerRepository.findByMobileJoinAddresses(mobile);

        if(customerList.size() > 0)
            return customerMapper.toDtoWithMappedAddresses(customerRepository.findByMobileJoinAddresses(mobile).get(0));

        return null;
    }
    public List<CustomerDTO> findByMobileContaining(String mobileOrEmail) {
        List<CustomerDTO> customerList = customerRepository.findTop5ByMobileContainingOrEmailContaining(mobileOrEmail, mobileOrEmail).stream().map(customerMapper::toDto).collect(Collectors.toList());
        return customerList;
    }

    public Customer registerUser(UserDTO userDTO, String password) {
        /*userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).ifPresent(existingUser -> {
            boolean removed = removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new LoginAlreadyUsedException();
            }
        });*/
        customerRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).ifPresent(existingUser -> {
            boolean removed = false; //removeNonActivatedUser(existingUser);
            if (!removed) {
                throw new EmailAlreadyUsedException();
            }
        });
        //User newUser = new User();
        String encryptedPassword = passwordEncoder.encode(password);
        //newUser.setLogin(userDTO.getLogin().toLowerCase());
        // new user gets initially a generated password
        //newUser.setPassword(encryptedPassword);
        //newUser.setFirstName(userDTO.getFirstName());
        //newUser.setLastName(userDTO.getLastName());
        //newUser.setEmail(userDTO.getEmail().toLowerCase());
        //newUser.setImageUrl(userDTO.getImageUrl());
        //newUser.setLangKey(userDTO.getLangKey());
        // new user is not active

        // new user gets registration key


        Customer newUser = new Customer();
        newUser.setPassword(encryptedPassword);
        newUser.setFirstname(userDTO.getFirstName());
        newUser.setLastname(userDTO.getLastName());
        newUser.setEmail(userDTO.getEmail().toLowerCase());
        newUser.setMobile(userDTO.getMobile());
        newUser.setActive(false);

        newUser.setSecureKey(RandomUtil.generateActivationKey());
        Set<Authority> authorities = new HashSet<>();
        authorityRepository.findById(AuthoritiesConstants.USER).ifPresent(authorities::add);
        newUser.setAuthorities(authorities);

        customerRepository.save(newUser);
        //this.clearUserCaches(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }
    @Transactional(readOnly = true)
    public Optional<Customer> getUserWithAuthorities() {
        return SecurityUtils.getCurrentUserLogin().flatMap(customerRepository::findOneWithAuthoritiesByEmail);
    }

    public Optional<Customer> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        return customerRepository.findOneByResetKey(key)
                .filter(user -> user.getResetDate().isAfter(Instant.now().minusSeconds(86400)))
                .map(user -> {
                    user.setPassword(passwordEncoder.encode(newPassword));
                    user.setResetKey(null);
                    user.setResetDate(null);
                    //this.clearUserCaches(user);
                    return user;
                });
    }

    @Transactional(readOnly = true)
    public Optional<Customer> getUserWithAuthorities(Long id) {
        return customerRepository.findOneWithAuthoritiesById(id);
    }

    public Optional<Customer> requestPasswordReset(String mail) {
        return customerRepository.findOneByEmailIgnoreCase(mail)
                //.filter(Customer::isActive)
                .map(user -> {
                    user.setResetKey(RandomUtil.generateResetKey());
                    user.setResetDate(Instant.now());
                    //this.clearUserCaches(user);
                    return user;
                });
    }

    public Optional<Customer> activateRegistration(String key, String email) {
        log.debug("Activating user for activation key {}", key);
        return customerRepository.findOneBySecureKeyAndEmail(key, email)
                .map(user -> {
                    // activate given user for the registration key.
                    user.setActive(true);
                    user.setSecureKey(null);
                    //this.clearUserCaches(user);
                    log.debug("Activated user: {}", user);
                    return user;
                });
    }
}
