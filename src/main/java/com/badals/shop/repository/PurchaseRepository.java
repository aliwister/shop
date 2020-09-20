package com.badals.shop.repository;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.Purchase;
import com.badals.shop.repository.projection.CartItemInfo;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.dto.PurchaseDTO;
import org.springframework.data.domain.Page;
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
   Page<Purchase> findAllByOrderByCreatedDateDesc(Pageable page);

   @Query("select p from Purchase p left join p.merchant order by p.id DESC")
   Page<Purchase> findForPurchaseList(Pageable page);

   @Query("from Purchase p left join fetch p.purchaseItems i left join fetch i.orderItems where p.id = ?1 order by i.sequence")
   Optional<Purchase>  findForUpdate(Long id);

   @Query("from Purchase p left join fetch p.merchant left join fetch p.deliveryAddress left join fetch p.purchaseItems i left join fetch i.orderItems where p.id = ?1 order by i.sequence")
   Optional<Purchase> findForPurchaseDetails(Long id);

   @Query(value="Select id, product_name as productName, outstanding as quantity, image, weight, price, url, sku, cost, order_id as orderId, product_id as productId, attributes from purchase_queue", nativeQuery=true)
   List<PurchaseQueue> getPurchaseQueue();

   @Query(value="SELECT pit.description as productName, pit.quantity - ifnull(SUM(si.quantity),0) as quantity, pp.image, pp.weight, pp.price, pp.url, pp.sku FROM shop.purchase_item pit  " +
           "left JOIN shop.purchase p ON p.id = pit.purchase_id  " +
           "left JOIN admin.purchase_shipment ps ON ps.purchase_item_id = pit.id  " +
           "left JOIN admin.shipment_item si ON ps.shipment_item_id = si.id  " +
           "LEFT JOIN shop.product pp ON pp.ref = pit.product_id  " +
           "WHERE p.order_state <> 'CANCELED' OR p.order_state IS null  " +
           "GROUP BY pit.id  " +
           "HAVING quantity > 0  ", nativeQuery=true)
   List<PurchaseQueue> findUnshipped();
}
