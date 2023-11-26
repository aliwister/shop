package com.badals.shop.repository;

import com.badals.shop.domain.tenant.TenantHashtag;
import org.springframework.cache.annotation.Cacheable;
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
    @Cacheable(value = "tags")
    @Query("from TenantHashtag p where p.tenantId = ?1")
    List<TenantHashtag> findForList(String tenantId);

}
