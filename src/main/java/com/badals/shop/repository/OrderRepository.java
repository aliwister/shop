package com.badals.shop.repository;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Order entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
   Optional<Order> findOrderByReferenceAndConfirmationKey(String reference, String confirmationKey);
   List<Order> findOrdersByCustomerOrderByCreatedDateDesc(Customer customer);
   List<Order> findAllByOrderStateInOrderByCreatedDateDesc(OrderState[] orderStates, Pageable page);
   List<Order> findAllByOrderByCreatedDateDesc(Pageable page);

   @Query("from Order o left join o.customer left join o.deliveryAddress where o.id = ?1")
   Optional<Order> findJoinCustomerJoinAddress(Long orderId);

   @Query("from Order o left join o.customer left join o.orderItems left join o.deliveryAddress where o.id = ?1")
   Optional<Order> findOrderJoinCustomerJoinOrderItemsJoinDeliveryAddress(Long id);
}
