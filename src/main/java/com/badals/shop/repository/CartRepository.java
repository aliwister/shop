package com.badals.shop.repository;

import com.badals.shop.domain.Cart;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.CartState;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findBySecureKey(String secureKey);
    List<Cart> findByCustomerAndCartStateOrderByIdDesc(Customer user, CartState cartState);
}
