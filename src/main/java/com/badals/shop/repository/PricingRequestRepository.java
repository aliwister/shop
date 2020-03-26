package com.badals.shop.repository;

import com.badals.shop.domain.PricingRequest;
import com.google.common.io.Files;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the PricingRequest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PricingRequestRepository extends JpaRepository<PricingRequest, Long> {

   @Query(" from PricingRequest u join fetch u.product p left join fetch p.parent where u.done = false or u.done is null")
   List<PricingRequest> findWithProduct();

   Boolean existsBySkuAndCreatedBy(String sku, String createdBy);

   List<PricingRequest> findAllByCreatedByAndDoneAndEmailSentIsNull(String email, boolean done);

   @Modifying
   @Query("update PricingRequest p set p.emailSent = true where p.id in :list")
   void updateEmailSentWhereIdIn(@Param("list") List<Long> list);
}
