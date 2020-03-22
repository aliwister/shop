package com.badals.shop.repository;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
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
   public Optional<Order> findOrderByReferenceAndConfirmationKey(String reference, String confirmationKey);
   public List<Order> findOrdersByCustomerOrderByCreatedDateDesc(Customer customer);
}
