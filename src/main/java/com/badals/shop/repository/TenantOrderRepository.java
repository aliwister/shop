package com.badals.shop.repository;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.web.rest.AuditResource;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
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

    //Optional<TenantOrder> findBySecureKey(String secureKey);

    //List<ProfileCart> findByCustomerAndCartStateOrderByIdDesc(Customer loginUser, CartState claimed);

    void refresh(TenantOrder cart);

    @Query("from TenantCart c left join fetch c.customer cc left join fetch cc.addresses where id = ?1")
    TenantCart getCartByCustomerJoinAddresses(Long id);


    Optional<TenantOrder> findOrderByReferenceAndConfirmationKey(String reference, String confirmationKey);

    List<TenantOrder> findOrdersByCustomerOrderByCreatedDateDesc(Customer loginUser, PageRequest of);

    @Query("select count(u) from TenantOrder u where u.customer in ?1")
    Integer countForCustomer(Customer loginUser);

    List<TenantOrder> findAllByOrderStateInOrderByCreatedDateDesc(List<OrderState> orderState, PageRequest of);

    @Query("select count(u) from TenantOrder u where u.orderState in ?1")
    Integer countForState(List<OrderState> orderState);

    //Optional<TenantOrder> findJoinCustomerJoinAddress(Long id);


    Optional<TenantOrder> findByReference(String id);

    @Query("select o from TenantOrder o left join fetch o.customer left join fetch o.orderItems oi left join fetch o.deliveryAddress left join fetch o.cart where o.id = ?1 or o.reference= ?2 order by oi.sequence")
    Optional<TenantOrder> findForOrderDetails(Long id, String valueOf);

    @Query("select o from TenantOrder o left join fetch o.customer left join fetch o.orderItems oi left join fetch o.deliveryAddress where o.id = ?1 and oi.id in ?2")
    Optional<TenantOrder> getOrderWithSomeOrderItems(Long orderId, ArrayList<Long> orderItems);
}
