package com.badals.shop.repository;

import com.badals.shop.domain.tenant.PageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageInfoRepository extends JpaRepository<PageInfo, Long> {
    List<PageInfo> findPageInfosBySlugAndTenantId(String slug, String tenantId);
    PageInfo findPageInfoBySlugAndTenantIdAndLanguage(String slug, String tenantId, String language);
    List<PageInfo> findPageInfosByTenantId(String tenantId);

}
