package com.badals.shop.repository;
import com.badals.shop.domain.Cart;
import com.badals.shop.domain.CartItem;
import com.badals.shop.repository.projection.CartItemInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the CartItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
   @Query("from CartItem c join fetch c.product")
   List<CartItem> findCartItemsWithProduct(Long id);

   @Query(value="Select p.price, p.ref, p.title, c.quantity, p.image, p.weight, p.sku, ms.availability, p.merchant_id from cart_item c join product p  on c.product_id = p.ref left join merchant_stock ms on ms.product_id = p.id where c.cart_id = :id ", nativeQuery=true)
   List<CartItemInfo> findCartItemsWithProductNative(@Param(value = "id") Long id);
}
