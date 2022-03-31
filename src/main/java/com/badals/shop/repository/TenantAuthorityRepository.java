package com.badals.shop.repository;

import com.badals.shop.domain.Authority;
import com.badals.shop.domain.tenant.TenantAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface TenantAuthorityRepository extends JpaRepository<TenantAuthority, Long> {
}
