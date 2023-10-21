package com.badals.shop.repository;

import com.badals.shop.domain.tenant.PageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PageInfoRepository extends JpaRepository<PageInfo, Long> {
    @Query("from PageInfo pi left join fetch pi.page p where p.slug = ?1 and p.tenantId = ?2 and pi.language = ?3")
    Optional<PageInfo> findPageInfoBySlugAndTenantIdAndLanguage(String slug, String tenantId, String language);
    List<PageInfo> findPageInfosByTenantId(String tenantId);

    void deleteByIdAndAndTenantId(Long id, String tenantId);
}
