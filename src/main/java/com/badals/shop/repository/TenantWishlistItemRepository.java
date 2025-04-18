package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantCartItem;
import com.badals.shop.domain.tenant.TenantWishListItem;
import com.badals.shop.repository.projection.CartItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data  repository for the WhishlistItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantWishlistItemRepository extends JpaRepository<TenantWishListItem, Long> {
    @Transactional
    Integer deleteTenantWishListItemByTenantIdAndWishlistIdAndProductId(String tenantId, Long wishlistId, Long productId);
    TenantWishListItem findTenantWishListItemByTenantIdAndWishlistIdAndProductId(String tenantId, Long wishlistId, Long productId);
}
