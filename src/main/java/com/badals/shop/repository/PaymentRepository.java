package com.badals.shop.repository;


import com.badals.shop.domain.Order;
import com.badals.shop.domain.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Spring Data  repository for the OrderPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
   @Query("from Payment p where p.order.id = ?1")
   List<Payment> findAllByOrderId(Long orderId);

   @Query("from Payment p where p.paymentMethod in (?1) and (?2 is null or p.settlementDate >= ?2) and (?3 is null or p.settlementDate <= ?3) and (?4 is null or p.order is null or p.order.customer.id = ?4 ) and (?5 is null or p.account = ?5 ) and (?6 is null or p.amount < ?6 ) order by p.id DESC")
   Page<Payment> findForTable(List<String> paymentMethods, Date from, Date to, Long customerId, String accountCode, String maxAmount , PageRequest of);

   @Modifying @Query("update Payment p set p.settlementDate = ?2 where p.id in ?1")
   void setSettlementDate(ArrayList<Long> ids, Date date);

   @Modifying @Query("update Payment p set p.processedDate = ?2 where p.id in ?1")
   void setProcessedDate(ArrayList<Long> ids, Date date);

   @Modifying @Query("update Payment p set p.account = ?2 where p.id in ?1")
   void setAccountingCode(ArrayList<Long> ids, String account);

}
