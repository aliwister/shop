package com.badals.shop.repository;

import com.badals.shop.domain.TenantCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantCartRepository extends JpaRepository<TenantCart, Long> {

    Optional<TenantCart> findBySecureKey(String secureKey);

    //List<ProfileCart> findByCustomerAndCartStateOrderByIdDesc(Customer loginUser, CartState claimed);

    void refresh(TenantCart cart);

    @Query("from TenantCart c left join fetch c.customer cc left join fetch cc.addresses where id = ?1")
    TenantCart getCartByCustomerJoinAddresses(Long id);
}
