package com.badals.shop.repository;


import com.badals.shop.domain.tenant.Checkout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CheckoutRepository extends JpaRepository<Checkout, Long> {

    Optional<Checkout> findBySecureKey(String secureKey);

    Optional<Checkout> findBySecureKeyAndCheckedOut(String secureKey, Boolean checkedOut);
}
