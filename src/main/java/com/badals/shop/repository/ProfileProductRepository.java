package com.badals.shop.repository;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProfileProduct;
import com.badals.shop.domain.enumeration.VariationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
public interface ProfileProductRepository extends JpaRepository<ProfileProduct, Long> {

    @Query("from ProfileProduct u where u.sku = ?1")
    Optional<ProfileProduct> findBySku(String asin);

    @Query("from ProfileProduct u where u.slug = ?1 and u.active = true and tenantId = ?2")
    Optional<ProfileProduct> findBySlug(String slug, Long tenantId);

    Optional<ProfileProduct> findOneByRef(String ref);

    Boolean existsBySku(String sku);

    @Query(value="Select weight from weight_lookup where sku = :sku", nativeQuery=true)
    BigDecimal lookupWeight(@Param(value = "sku") String sku);

    @Query("from ProfileProduct u  left join fetch u.stock left join fetch u.children c left join fetch c.stock where u.sku = ?1 and u.merchantId = ?2")
    Optional<Product> findBySkuJoinChildren(String asin, Long merchantId);

    @Query("from ProfileProduct u left join fetch u.stock left join fetch u.children c left join fetch c.stock where u.id = ?1 and u.tenantId = ?2")
    Optional<ProfileProduct> findByIdJoinChildren(Long id, Long tenantId);

    Optional<ProfileProduct> findOneBySku(String sku);

    Optional<ProfileProduct> findOneBySkuAndMerchantId(String sku, Long merchantId);

    List<ProfileProduct> findBySkuInAndMerchantId(Set<String> skus, Long merchantId);

    List<ProfileProduct> findByVariationTypeInAndPriceIsNotNullOrderByCreatedDesc(List<VariationType> types, Pageable pageable);

    List<ProfileProduct> findByVariationTypeInAndPriceIsNotNullAndStubEqualsAndHashtagsIsNotNullOrderByCreatedDesc(List<VariationType> types, Pageable pageable, Boolean isStub);


    @Query("from ProfileProduct u  left join fetch u.stock where u.tenantId = ?1 and (?2 is null or u.title like ?2) and u.variationType <> ?3")
    List<ProfileProduct> listForTenantAll(Long tenantId, String like, VariationType child, Pageable pageable);

    @Query("from ProfileProduct u where u.merchantId = ?1 and u.active = ?2 and (?3 is null or u.title like ?3) and u.variationType <> ?4")
    List<ProfileProduct> listForTenantActive(Long merchantId, Boolean active, String like, VariationType child, Pageable pageable);

    @Query("select count(u) from ProfileProduct u where u.tenantId = ?1 and u.active = ?2 and (?3 is null or u.title like ?3) and u.variationType <> ?4")
    Integer countForTenantActive(Long tenantId, Boolean active, String like, VariationType child);

    @Query("select count(u) from ProfileProduct u where u.tenantId = ?1 and (?2 is null or u.title like ?2) and u.variationType <> ?3")
    Integer countForTenant(Long tenantId, String like, VariationType child);

    @Query("from ProfileProduct u where u.sku in ?1 and u.parentId is null")
    List<ProfileProduct> findAllBySku(Set<String> keySet);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ProfileProduct u set u.parentId = ?1 where u.sku in ?2")
    void updateParentAllBySku(Long parentId, Set<String> keySet);


    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("update ProfileProduct u set u.deleted = ?1, u.active = 0 where u.id in ?2")
    void delete(Boolean delete, Long id);

    @Query(value="select * from profileshop.product u where :tag MEMBER OF(u.hashtags) and u.tenant_id = :tenantId", nativeQuery=true)
    List<ProfileProduct> findActiveTagProductsForTenant(@Param(value = "tag") String tag, @Param(value = "tenantId") Long tenantId);

}