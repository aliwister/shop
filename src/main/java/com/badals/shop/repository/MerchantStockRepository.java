package com.badals.shop.repository;

import com.badals.shop.domain.MerchantStock;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the MerchantStock entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MerchantStockRepository extends JpaRepository<MerchantStock, Long> {

}
