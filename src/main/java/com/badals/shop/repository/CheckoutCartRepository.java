package com.badals.shop.repository;

import com.badals.shop.domain.CheckoutCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckoutCartRepository extends JpaRepository<CheckoutCart, Long> {

    Optional<CheckoutCart> findBySecureKey(String secureKey);

    Optional<CheckoutCart> findBySecureKeyAndCheckedOut(String secureKey, Boolean checkedOut);
}
