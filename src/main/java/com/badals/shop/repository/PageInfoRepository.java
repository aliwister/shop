package com.badals.shop.repository;

import com.badals.shop.domain.tenant.PageInfo;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PageInfoRepository extends CrudRepository<PageInfo, Long> {
    List<PageInfo> findPageInfosBySlugAndTenantId(String slug, String tenantId);
    PageInfo findPageInfoBySlugAndTenantIdAndLanguage(String slug, String tenantId, String language);
    List<PageInfo> findPageInfosByTenantId(String tenantId);

}
