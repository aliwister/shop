package com.badals.shop.repository;

import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.OverrideType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ProductOverride entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductOverrideRepository extends JpaRepository<ProductOverride, Long> {

   List<ProductOverride> findBySku(String sku);
   List<ProductOverride> findBySkuIn(List<String> asins);

   Optional<ProductOverride> findBySkuEqualsAndTypeEquals(String sku, OverrideType type);
}
