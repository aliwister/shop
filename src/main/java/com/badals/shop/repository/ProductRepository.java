package com.badals.shop.repository;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.enumeration.VariationType;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;


/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("from Product u where u.sku = ?1")
    Optional<Product> findBySkuJoinCategories(String asin);

    @Query("from Product u where u.slug = ?1 and u.active = true")
    Optional<Product> findBySlugJoinCategories(String slug);

    @Query("select u.products from Category u where u.slug = ?1")
    List<Product> findAllByCategorySlug(String slug);

    Optional<Product> findOneByRef(Long ref);

    Boolean existsBySku(String sku);

    @Query(value="Select weight from weight_lookup where sku = :sku", nativeQuery=true)
    BigDecimal lookupWeight(@Param(value = "sku") String sku);

    @Query("from Product u left join fetch u.productLangs left join fetch u.merchantStock left join fetch u.children c left join fetch c.productLangs left join fetch c.merchantStock where u.sku = ?1 and u.merchantId = ?2")
    Optional<Product> findBySkuJoinChildren(String asin, Long merchantId);

    @Query("from Product u left join fetch u.productLangs left join fetch u.merchantStock left join fetch u.children c left join fetch c.productLangs left join fetch c.merchantStock where u.id = ?1 and u.merchantId = ?2")
    Optional<Product> findByIdJoinChildren(Long id, Long merchantId);

    Optional<Product> findOneBySku(String sku);

    Optional<Product> findOneBySkuAndMerchantId(String sku, Long merchantId);

    List<Product> findBySkuInAndMerchantId(Set<String> skus, Long merchantId);

    List<Product> findByVariationTypeInAndPriceIsNotNullOrderByCreatedDesc(List<VariationType> types, Pageable pageable);

    @Query("from Product u left join fetch u.productLangs left join fetch u.merchantStock left join fetch u.categories where u.merchantId = ?1")
    List<Product> listForTenantAll(Long merchantId, Pageable pageable);

    @Query("from Product u where u.merchantId = ?1 and u.active = ?2 and (?3 is null or u.title like ?3) and u.variationType <> ?4")
    List<Product> listForTenantActive(Long merchantId, Boolean active, String like, VariationType child, Pageable pageable);

    @Query("select count(u) from Product u where u.merchantId = ?1 and u.active = ?2 and (?3 is null or u.title like ?3) and u.variationType <> ?4")
    Integer countForTenantActive(Long merchantId, Boolean active, String like, VariationType child);

    @Query("select count(u) from Product u where u.merchantId = ?1")
    Integer countForTenant(Long merchantId);

    @Query("from Product u where u.sku = ?1")
    Optional<Product> findBySku(String asin);

    @Query("from Product u where u.sku in ?1 and u.parentId is null")
    List<Product> findAllBySku(Set<String> keySet);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update Product u set u.parentId = ?1 where u.sku in ?2")
    void updateParentAllBySku(Long parentId, Set<String> keySet);
}
