package com.badals.shop.repository;
import com.badals.shop.domain.Customer;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

   public List<Customer> findTop5ByMobileContainingOrEmailContaining(String mobile, String email);

   public Optional<Customer> findByEmailIgnoreCase(String email);

   @Query("from Customer c left join fetch c.addresses where c.mobile = ?1")
   public List<Customer> findByMobileJoinAddresses(String mobile);

   @Query("from Customer c left join fetch c.addresses where c.id = ?1")
   public Optional<Customer> findByIdJoinAddresses(Long id);

   String USERS_BY_LOGIN_CACHE = "usersByLogin";

   String USERS_BY_EMAIL_CACHE = "usersByEmail";
   Optional<Customer> findByEmail(String email);
   Optional<Customer> findById(Long id);
   boolean existsByEmail(String email);

   Optional<Customer> findOneBySecureKey(String activationKey);

   Optional<Customer> findOneBySecureKeyAndEmail(String activationKey, String email);
   //List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);


   Optional<Customer> findOneByResetKey(String resetKey);

   Optional<Customer> findOneByEmailIgnoreCase(String email);

   //Optional<User> findOneByLogin(String login);

   @EntityGraph(attributePaths = "authorities")
   Optional<Customer> findOneWithAuthoritiesById(Long id);

   @EntityGraph(attributePaths = "authorities")
   //@Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
   Optional<Customer> findOneWithAuthoritiesByEmail(String login);

   Optional<Customer> findOneByEmailIgnoreCaseAndTenantId(String email, String tenantId);

    List<Customer> findAllByIdIn(Set<Long> ids);
}
