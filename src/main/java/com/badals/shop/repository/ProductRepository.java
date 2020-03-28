package com.badals.shop.repository;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.enumeration.VariationType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("from Product u where u.sku = ?1")
    Optional<Product> findBySkuJoinCategories(String asin);

    @Query("from Product u where u.slug = ?1")
    Optional<Product> findBySlugJoinCategories(String slug);

    @Query("select u.products from Category u where u.slug = ?1")
    List<Product> findAllByCategorySlug(String slug);

    Optional<Product> findOneByRef(Long ref);

    Boolean existsBySku(String sku);

    @Query("from Product u left join fetch u.productLangs left join fetch u.merchantStock left join fetch u.children c left join fetch c.productLangs left join fetch c.merchantStock where u.sku = ?1")
    Optional<Product> findBySkuJoinChildren(String asin);

    Optional<Product> findOneBySku(String sku);

    List<Product> findByVariationTypeInAndPriceIsNotNullOrderByCreatedDesc(List<VariationType> types, Pageable pageable);
}
