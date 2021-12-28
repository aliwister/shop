package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the Hashtag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantHashtagRepository extends JpaRepository<TenantHashtag, Long> {

   @Query("from TenantHashtag p where p.tenant.id = ?1")
   List<TenantHashtag> findForList(Long tenantId);

}
