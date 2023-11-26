package com.badals.shop.repository;

import com.badals.shop.domain.tenant.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Long> {
    Page findPageBySlug(String slug);
    Page findPageBySlugAndTenantIdAndEnabled(String slug, String tenant_id, Boolean enabled);
    List<Page> findAllByTenantIdAndEnabled(String tenant_id, Boolean enabled);


}
