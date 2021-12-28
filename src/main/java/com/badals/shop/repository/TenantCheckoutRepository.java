package com.badals.shop.repository;

import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantCheckout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantCheckoutRepository extends JpaRepository<TenantCheckout, Long> {

    Optional<TenantCheckout> findBySecureKey(String secureKey);

    Optional<TenantCheckout> findBySecureKeyAndCheckedOut(String secureKey, Boolean checkedOut);
}
