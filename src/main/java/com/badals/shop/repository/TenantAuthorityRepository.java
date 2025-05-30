package com.badals.shop.repository;

import com.badals.shop.domain.Authority;
import com.badals.shop.domain.tenant.TenantAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface TenantAuthorityRepository extends JpaRepository<TenantAuthority, Long> {

    List<TenantAuthority> getAllByTenantId(String tenantId);

}
