package com.badals.shop.repository;

import com.badals.shop.domain.Tenant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Tenant entity.
 */
@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {

    @Query(value = "select distinct tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers",
        countQuery = "select count(distinct tenant) from Tenant tenant")
    Page<Tenant> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers")
    List<Tenant> findAllWithEagerRelationships();


    @Query("select distinct tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers c where c.email = ?1")
    List<Tenant> findTenantAndMerchantByCustomer(String id);

    @Query("select tenant from Tenant tenant left join fetch tenant.merchants left join fetch tenant.customers where tenant.id =:id")
    Optional<Tenant> findOneWithEagerRelationships(@Param("id") Long id);

}
