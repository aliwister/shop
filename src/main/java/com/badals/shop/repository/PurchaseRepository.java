package com.badals.shop.repository;

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

   @Query("select p from Purchase p left join p.merchant where (?1 is null or p.merchant.name like CONCAT(?1, '%') or p.ref like CONCAT('%', ?1, '%')) order by p.id DESC")
   Page<Purchase> findForPurchaseList(Pageable page, String search);

   @Query("from Purchase p left join fetch p.purchaseItems i left join fetch i.orderItems where p.id = ?1 order by i.sequence")
   Optional<Purchase>  findForUpdate(Long id);

   @Query("from Purchase p left join fetch p.merchant left join fetch p.deliveryAddress left join fetch p.purchaseItems i left join fetch i.orderItems where p.id = ?1 order by i.sequence")
   Optional<Purchase> findForPurchaseDetails(Long id);

   @Query(value="select " +
           "    `o`.`id` as `oid`, " +
           "    `oi`.`id` as `id`, " +
           "    `oi`.`product_name` as `productName`, " +
           "    `oi`.`quantity` as `quantity`, " +
           "    `oi`.`price` as `price`, " +
           "    `oi`.`comment` as `comment`, " +
           "    `oi`.`sequence` as `sequence`, " +
           "    `oi`.`shipping_instructions` as `shipping_instructions`, " +
           "    `oi`.`order_id` as `orderId`, " +
           "    `oi`.`image` as `image`, " +
           "    `oi`.`unit` as `unit`, " +
           "    `oi`.`weight` as `weight`, " +
           "    `oi`.`line_total` as `line_total`, " +
           "    ifnull(`oi`.`url`, " +
           "    ( " +
           "    select " +
           "        `pp`.`url` " +
           "    from " +
           "        `profileshop`.`product` `pp` " +
           "    where " +
           "        (`pp`.`ref` = `oi`.`product_id`))) as `url`, " +
           "    ( " +
           "    select " +
           "        `pp`.`merchant_id` " +
           "    from " +
           "        `profileshop`.`product` `pp` " +
           "    where " +
           "        (`pp`.`ref` = `oi`.`product_id`)) as `merchantId`, " +
           "    `oi`.`sku` as `sku`, " +
           "    `oi`.`product_id` as `productId`, " +
           "    ifnull(sum(`pui`.`quantity`), " +
           "    0) as `purchased`, " +
           "    (`oi`.`quantity` - ifnull(sum(`pui`.`quantity`), 0)) as `quantity`, " +
           "    ifnull(( " +
           "    select " +
           "        sum(`os`.`quantity`) " +
           "    from " +
           "        ((`admin`.`order_shipment` `os` " +
           "    join `admin`.`shipment_item` `si` on " +
           "        ((`os`.`shipment_item_id` = `si`.`id`))) " +
           "    join `admin`.`shipment` `s` on " +
           "        ((`s`.`id` = `si`.`shipment_id`))) " +
           "    where " +
           "        ((`os`.`order_item_id` = `oi`.`id`) " +
           "        and (`s`.`shipment_type` = 'CUSTOMER')) " +
           "    group by " +
           "        `os`.`order_item_id`), " +
           "    0) as `issued` " +
           "from " +
           "    (((`profileshop`.`order_item` `oi` " +
           "join `profileshop`.`jhi_order` `o` on " +
           "    ((`o`.`id` = `oi`.`order_id`))) " +
           "left join `shop`.`purchase_item_order_item` `pioi` on " +
           "    ((`pioi`.`order_item_id` = `oi`.`id`))) " +
           "left join `shop`.`purchase_item` `pui` on " +
           "    ((`pui`.`id` = `pioi`.`purchase_item_id`))) " +
           "where " +
           "    ((`o`.`state` = 'PAYMENT_ACCEPTED') " +
           "    or ((`o`.`state` = 'DELIVERED') " +
           "    and (`o`.`state` <> 'CLOSED'))) " +
           "    and o.tenant_id = 'badals' " +
           "group by " +
           "    `oi`.`id` " +
           "having " +
           "    ((`oi`.`quantity` > `purchased`) " +
           "    and (`issued` < `quantity`)) " +
           "order by " +
           "    `oi`.`id` desc ", nativeQuery=true)
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
