package com.badals.shop.repository;

import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.domain.tenant.TenantAuthority;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Tenant entity.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

/*    @Query(value = "select distinct tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers",
        countQuery = "select count(distinct tenant) from Tenant tenant")
    Page<Tenant> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers")
    List<Tenant> findAllWithEagerRelationships();


    @Query("select distinct tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers c where c.email = ?1")
    List<Tenant> findTenantAndMerchantByCustomer(String id);

    @Query("select tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers c where c.email = ?1 and tenant.tenantId = ?2")
    Optional<Tenant> getIsTenantForCustomer(String id, String tenantId);

    @Query("select tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers where tenant.id =:id")
    Optional<Tenant> findOneWithEagerRelationships(@Param("id") Long id);*/

    @Query
    Optional<Tenant> findByNameIgnoreCase(String name);

    @Query
    Optional<Tenant> findByTenantId(String name);

    @Query("from TenantAuthority a where a.userId = ?1 and a.tenantId = ?2 ")
    List<TenantAuthority> getTenantAuthorities(Long userId, String tenantId);

    @Query("select a.tenant from TenantAuthority a join a.tenant where a.userId = ?1")
    List<Tenant> findTenantsForUser(Long userId);
}
