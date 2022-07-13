package com.badals.shop.repository;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.domain.tenant.TenantOrderItem;
import com.badals.shop.repository.projection.AggOrderEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantOrderItemRepository extends JpaRepository<TenantOrderItem, Long> {
}

