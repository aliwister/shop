package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.repository.TenantWishlistItemRepository;
import com.badals.shop.repository.TenantWishlistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.badals.shop.domain.tenant.TenantWishList}.
 */
@Service
@Transactional
public class TenantWishListService {

    private final Logger log = LoggerFactory.getLogger(TenantWishListService.class);
    private final TenantWishlistRepository tenantWishlistRepository;
    private final TenantWishlistItemRepository tenantWishlistItemRepository;

    public TenantWishListService(TenantWishlistRepository tenantWishlistRepository, TenantWishlistItemRepository tenantWishlistItemRepository) {
        this.tenantWishlistRepository = tenantWishlistRepository;
        this.tenantWishlistItemRepository = tenantWishlistItemRepository;
    }


}
