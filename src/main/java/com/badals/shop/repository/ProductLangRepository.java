package com.badals.shop.repository;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ProductLang entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductLangRepository extends JpaRepository<ProductLang, Long> {

    List<ProductLang> findAllByProductId(Long id);
}
