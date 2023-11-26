package com.badals.shop.service;

import com.badals.shop.domain.tenant.Page;
import com.badals.shop.domain.tenant.TenantFaqCategory;
import com.badals.shop.domain.tenant.TenantFaqQA;
import com.badals.shop.repository.FaqCategoryRepository;
import com.badals.shop.repository.FaqQARepository;
import com.badals.shop.repository.PageInfoRepository;
import com.badals.shop.repository.PageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PageInfoPublicService {
    private final Logger log = LoggerFactory.getLogger(PageInfoPublicService.class);


    private final PageInfoRepository pageInfoRepository;
    private final PageRepository pageRepository;

    public PageInfoPublicService(PageInfoRepository pageInfoRepository, PageRepository pageRepository){
        this.pageInfoRepository = pageInfoRepository;
        this.pageRepository = pageRepository;
    }

    public Page getPageInfosBySlug(String tenant_id, String slug) {
        return pageRepository.findPageBySlugAndTenantIdAndEnabled(slug, tenant_id, true);
    }

    public List<Page> getPages(String tenant_id) {
        return pageRepository.findAllByTenantIdAndEnabled(tenant_id,true);
    }



}
