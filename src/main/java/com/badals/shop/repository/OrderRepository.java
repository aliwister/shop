package com.badals.shop.repository;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderState;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


import java.util.ArrayList;
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
   List<Order> findAllByOrderStateInOrderByCreatedDateDesc(List<OrderState> orderStates, Pageable page);
   List<Order> findAllByOrderByCreatedDateDesc(Pageable page);

   List<Order> findByIdBetween(Long from, Long to);

   @Query("from Order o left join o.customer left join fetch o.deliveryAddress where o.id = ?1")
   Optional<Order> findJoinCustomerJoinAddress(Long orderId);

   @Query("select o from Order o left join fetch o.customer left join fetch o.orderItems oi left join fetch o.deliveryAddress left join fetch o.payments left join fetch oi.purchaseItem left join fetch o.cart where o.id = ?1 or o.reference= ?2 order by oi.sequence")
   Optional<Order> findForOrderDetails(Long id, String ref);

   @Query("select o from Order o left join fetch o.customer left join fetch o.orderItems oi left join fetch o.deliveryAddress where o.id = ?1 and oi.id in ?2")
   Optional<Order> getOrderWithSomeOrderItems(Long orderId, ArrayList<Long> orderItems);

   Optional<Order> findByReference(String ref);

   @Query("select count(u) from Order u where u.orderState in ?1")
   Integer countForState(List<OrderState> orderState);

}
