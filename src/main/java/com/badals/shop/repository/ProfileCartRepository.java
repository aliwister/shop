package com.badals.shop.repository;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.ProfileCart;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.enumeration.CartState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProfileCartRepository extends JpaRepository<ProfileCart, Long> {

    Optional<ProfileCart> findBySecureKey(String secureKey);

    //List<ProfileCart> findByCustomerAndCartStateOrderByIdDesc(Customer loginUser, CartState claimed);

    void refresh(ProfileCart cart);
}
