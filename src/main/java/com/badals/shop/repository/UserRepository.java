package com.badals.shop.repository;

import com.badals.shop.domain.Customer;

import com.badals.shop.domain.User;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link User} entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    String USERS_BY_LOGIN_CACHE = "usersByLogin";

    String USERS_BY_EMAIL_CACHE = "usersByEmail";
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
    boolean existsByEmail(String email);

    //Optional<User> findOneByActivationKey(String activationKey);

    //Optional<User> findOneByActivationKeyAndEmail(String activationKey, String email);
    //List<User> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(Instant dateTime);


    //Optional<User> findOneByResetKey(String resetKey);

    Optional<User> findOneByEmailIgnoreCase(String email);

    //Optional<User> findOneByLogin(String login);

/*    @EntityGraph(attributePaths = "authorities")
    Optional<User> findOneWithAuthoritiesById(Long id);

    @EntityGraph(attributePaths = "authorities")
    @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
    Optional<User> findOneWithAuthoritiesByEmail(String login);*/

    //@EntityGraph(attributePaths = "authorities")
    //@Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
    //Optional<User> findOneWithAuthoritiesByEmail(String email);

    //Page<User> findAllByLoginNot(Pageable pageable, String login);
}
