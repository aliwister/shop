package com.badals.shop.repository;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.Purchase;
import com.badals.shop.repository.projection.CartItemInfo;
import com.badals.shop.repository.projection.PurchaseQueue;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
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

   @Query("from Purchase p left join fetch p.merchant order by p.id DESC")
   List<Purchase> findForPurchaseList(Pageable page);

   @Query("from Purchase p left join fetch p.purchaseItems i left join fetch i.orderItem where p.id = ?1")
   Optional<Purchase>  findForUpdate(Long id);

   @Query("from Purchase p left join fetch p.merchant left join fetch p.deliveryAddress left join fetch p.purchaseItems i left join fetch i.orderItem where p.id = ?1 order by i.sequence")
   Optional<Purchase> findForPurchaseDetails(Long id);

   @Query(value="Select id, product_name as productName, outstanding as quantity, image, weight, price, url, sku, cost, order_id as orderId, product_id as productId, attributes from purchase_queue", nativeQuery=true)
   List<PurchaseQueue> getPurchaseQueue();
}
