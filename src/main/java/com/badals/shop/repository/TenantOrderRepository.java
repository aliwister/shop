package com.badals.shop.repository;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.projection.AggOrderEntry;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Cart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantOrderRepository extends JpaRepository<TenantOrder, Long> {

    void refresh(TenantOrder cart);

    @Query("from TenantCart c left join fetch c.customer cc left join fetch cc.addresses where id = ?1")
    TenantCart getCartByCustomerJoinAddresses(Long id);


    Optional<TenantOrder> findOrderByReferenceAndConfirmationKey(String reference, String confirmationKey);

    List<TenantOrder> findOrdersByCustomerOrderByCreatedDateDesc(Customer loginUser, PageRequest of);

    @Query("select count(u) from TenantOrder u where u.customer in ?1")
    Integer countForCustomer(Customer loginUser);

    Page<TenantOrder> findAllByOrderStateInOrderByCreatedDateDesc(List<OrderState> orderState, Pageable page);

    @Query("select count(u) from TenantOrder u where u.orderState in ?1")
    Integer countForState(List<OrderState> orderState);

    Optional<TenantOrder> findByReference(String id);

    @Query("select o from TenantOrder o left join fetch o.customer left join fetch o.orderItems oi left join fetch o.deliveryAddress left join fetch o.payments left join fetch o.cart where o.id = ?1 or o.reference= ?2 order by oi.sequence")
    Optional<TenantOrder> findForOrderDetails(Long id, String valueOf);

    @Query("select o from TenantOrder o left join fetch o.customer left join fetch o.orderItems oi left join fetch o.deliveryAddress where o.id = ?1 and oi.id in ?2")
    Optional<TenantOrder> getOrderWithSomeOrderItems(Long orderId, ArrayList<Long> orderItems);

    @Query("SELECT function('DATE_FORMAT',function('CONVERT_TZ', o.createdDate, '+00:00', ?2), ?1) as period, COUNT(o) as count, sum(o.total) as total FROM TenantOrder o group by function('DATE_FORMAT',function('CONVERT_TZ', o.createdDate, '+00:00', ?2), ?1)")
    List<AggOrderEntry> aggOrderReport(String period, String timezone);

/*    @Query(nativeQuery = true, value="SELECT DATE_FORMAT(CONVERT_TZ(o.created_date,'+00:00', '+4:00'), '%Y-%m-%d %H:00:00') as `period`, COUNT(1) as `count`, sum(o.total) as `total` FROM profileshop.jhi_order o group by DATE_FORMAT(CONVERT_TZ(o.created_date,'+00:00', '+4:00'), '%Y-%m-%d %H')")
    List<AggregateOrderReport> aggOrderNativeReport();*/

    @Query(value="select t.id " +
            "from  (select :s0 as id " +
            "union all select :s1 " +
            "union all select :s2 " +
            "union all select :s3 " +
            "union all select :s4 " +
            "union all select :s5 " +
            "union all select :s6 " +
            "union all select :s7 " +
            "union all select :s8 " +
            "union all select :s9 " +
            ") as t  " +
            "where not exists ( " +
            "select 1 from  " +
            "jhi_order o " +
            "where o.reference = t.id " +
            ")  " +
            "limit 1 ", nativeQuery = true)
    String getFirstUnused(
            @Param("s0") String option0,
            @Param("s1") String option1,
            @Param("s2") String option2,
            @Param("s3") String option3,
            @Param("s4") String option4,
            @Param("s5") String option5,
            @Param("s6") String option6,
            @Param("s7") String option7,
            @Param("s8") String option8,
            @Param("s9") String option9);
}

