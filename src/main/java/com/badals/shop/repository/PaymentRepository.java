package com.badals.shop.repository;


import com.badals.shop.domain.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the OrderPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
   @Query("from Payment p where p.order.id = ?1")
   List<Payment> findAllByOrderId(Long orderId);
}
