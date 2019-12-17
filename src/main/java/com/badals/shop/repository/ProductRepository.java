package com.badals.shop.repository;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductWrapper;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Product entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductRepository extends JpaRepository<ProductWrapper, Long> {

}
