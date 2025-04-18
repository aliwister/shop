package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantCartItem;
import com.badals.shop.repository.projection.CartItemInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the CartItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantCartItemRepository extends JpaRepository<TenantCartItem, Long> {
   @Query("from TenantCartItem c inner join fetch c.product")
   List<TenantCartItem> findCartItemsWithProduct(Long id);

   @Query(value="select p.price->> :currencyPath as price, p.price->>'$.base' as baseCurrency, json_unquote(JSON_EXTRACT(p.price, concat('$.prices.',p.price->>'$.base'))) as basePrice, p.ref, p.title, c.quantity, p.image, p.weight, p.sku, ms.availability, p.merchant_id as merchantId from profileshop.cart_item c join profileshop.product p  on c.product_id = p.ref left join profileshop.stock ms on ms.product_id = p.id where c.cart_id = :id ", nativeQuery=true)
   List<CartItemInfo> findCartItemsWithProductNative(@Param(value = "id") Long id, @Param(value = "currencyPath") String currencyPath);
}
