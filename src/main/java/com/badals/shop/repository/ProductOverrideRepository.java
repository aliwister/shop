package com.badals.shop.repository;

import com.badals.shop.domain.ProductOverride;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the ProductOverride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOverrideRepository extends JpaRepository<ProductOverride, Long> {

    @Query("select productOverride from ProductOverride productOverride where productOverride.createdBy.login = ?#{principal.username}")
    List<ProductOverride> findByCreatedByIsCurrentUser();

    @Query("select productOverride from ProductOverride productOverride where productOverride.lastModifiedBy.login = ?#{principal.username}")
    List<ProductOverride> findByLastModifiedByIsCurrentUser();

    List<ProductOverride> findBySku(String sku);

   List<ProductOverride> findBySkuIn(List<String> asins);
}
