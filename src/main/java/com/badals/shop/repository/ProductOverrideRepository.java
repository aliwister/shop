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

    List<ProductOverride> findBySku(String sku);

   List<ProductOverride> findBySkuIn(List<String> asins);
}
