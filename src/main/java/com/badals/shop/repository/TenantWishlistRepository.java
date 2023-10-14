package com.badals.shop.repository;

import com.badals.shop.domain.enumeration.CartState;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantWishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Wishlist entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantWishlistRepository extends JpaRepository<TenantWishList, Long> {

    Optional<TenantWishList> findById(Long id);
    List<TenantWishList> findByCustomerIdOrderByIdDesc(Long id);
    void refresh(TenantWishList tenantWishList);
    TenantWishList findTenantWishListByTenantIdAndAndCustomerId(String tenantId, Long customerId);

}
