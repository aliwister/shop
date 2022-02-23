package com.badals.shop.repository;


import com.badals.shop.domain.Payment;
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


}
