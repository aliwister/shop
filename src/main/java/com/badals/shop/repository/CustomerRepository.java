package com.badals.shop.repository;
import com.badals.shop.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;


/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {


   public Optional<Customer> findByEmailIgnoreCase(String email);

   @Query("from Customer c join fetch c.addresses where c.mobile = ?1")
   public Optional<Customer> findByMobileJoinAddresses(String mobile);

   @Query("from Customer c left join fetch c.addresses where c.id = ?1")
   public Optional<Customer> findByIdJoinAddresses(Long id);
}
