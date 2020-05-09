package com.badals.shop.repository;
import com.badals.shop.domain.Customer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
   public Optional<Customer> findByEmail(String email);

   @Query("from Customer c left join fetch c.addresses where c.id = ?1")
   public Optional<Customer> findByIdJoinAddresses(Long id);
}
