package com.badals.shop.repository;

import com.badals.shop.domain.tenant.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {
    Page findPageBySlug(String slug);
}
