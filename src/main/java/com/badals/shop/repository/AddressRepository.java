package com.badals.shop.repository;
import com.badals.shop.domain.Address;
import com.badals.shop.domain.Customer;
import com.badals.shop.service.dto.AddressDTO;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Address entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

   @Query(value="select exists(select 1 from jhi_order o where o.invoice_address_id = :id or o.delivery_address_id = :id) as ret", nativeQuery = true)
   Integer findInOrders(@Param(value = "id") Long id);

   @Modifying
   @Query("update Address a set a.active = 0 where a.id = ?1")
   void retireAddress(Long id);

   @Modifying
   @Query("update Address a set a.active = 0, a.deleted = 1 where a.id = ?1")
   void deleteAddressByFlag(Long id);

   @Query("select id from Country where isoCode = ?1")
   Long findCountryByCode(String countryCode);

   List<Address> findAddressesByCustomerEqualsAndActiveIsTrueAndDeletedIsFalse(Customer c);
}
