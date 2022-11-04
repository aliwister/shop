package com.badals.shop.repository;


import com.badals.shop.domain.tenant.TenantPayment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Spring Data  repository for the OrderPayment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantPaymentRepository extends JpaRepository<TenantPayment, Long> {
   @Query("from TenantPayment p where p.order.id = ?1")
   List<TenantPayment> findAllByOrderId(Long orderId);

   @Modifying
   @Query("update TenantPayment p set p.voided = true where p.order.id = ?1")
   void voidOrderPayments(Long orderId);


   @Query("select p from TenantPayment p left join p.order o left join o.customer c where p.paymentMethod in (?1) and (?2 is null or p.settlementDate >= ?2) and (?3 is null or p.settlementDate <= ?3) and (?4 is null or p.order is null or p.order.customer.id = ?4 ) and (?5 is null or p.account = ?5 ) and (?6 is null or p.amount < ?6 ) and (?7 = false or p.settlementDate is null) and p.voided = false order by p.id DESC")
   Page<TenantPayment> findForTable(List<String> paymentMethods, Date from, Date to, Long customerId, String accountCode, String maxAmount, Boolean unsettledOnly, PageRequest of);

   @Modifying @Query("update TenantPayment p set p.settlementDate = ?2 where p.id in ?1")
   void setSettlementDate(ArrayList<Long> ids, Date date);

   @Modifying @Query("update TenantPayment p set p.processedDate = ?2 where p.id in ?1")
   void setProcessedDate(ArrayList<Long> ids, Date date);

   @Modifying @Query("update TenantPayment p set p.account = ?2 where p.id in ?1")
   void setAccountingCode(ArrayList<Long> ids, String account);

   @Modifying(flushAutomatically = true, clearAutomatically = true)
   @Query("update TenantPayment p set p.captureId = ?2 where p.trackId in ?1")
   void updateCaptureId(Long cartId, String captureId);
   
}
