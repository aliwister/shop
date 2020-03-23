package com.badals.shop.repository;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.Purchase;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Purchase entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
   List<Purchase> findAllByOrderByCreatedDateDesc(Pageable page);

   @Query("from Purchase p left join p.merchant left join p.purchaseItems where p.id = ?1")
   Optional<Purchase> findPurchaseJoinMerchantJoinPurchaseItemsJoinDeliveryAddress(Long id);
}
