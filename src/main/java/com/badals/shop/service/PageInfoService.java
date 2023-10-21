package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.tenant.Page;
import com.badals.shop.domain.tenant.PageInfo;
import com.badals.shop.repository.PageInfoRepository;
import com.badals.shop.repository.PageRepository;
import com.badals.shop.service.dto.PageInfoInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional
public class PageInfoService {
    private final Logger log = LoggerFactory.getLogger(PageInfoService.class);
    private final PageInfoRepository pageInfoRepository;
    private final PageRepository pageRepository;
    public PageInfoService(PageInfoRepository pageInfoRepository, PageRepository pageRepository) {
        this.pageInfoRepository = pageInfoRepository;
        this.pageRepository = pageRepository;
    }

    public Page createPageInfo(PageInfoInput pageInfoInput) {
        Page page = pageRepository.findPageBySlugAndTenantId(pageInfoInput.getSlug(),  TenantContext.getCurrentProfile());
        if (page == null){
            Page new_page = new Page();
            new_page.setSlug(pageInfoInput.getSlug());
            new_page.setIsImportant(pageInfoInput.getIsImportant());
            page = pageRepository.save(new_page);
        }
        PageInfo pageInfo = new PageInfo();
        pageInfo.setLanguage(pageInfoInput.getLanguage());
        pageInfo.setInfo(pageInfoInput.getInfo());
        pageInfo.setPage(page);
        page.getPageInfos().add(pageInfo);
                    return pageRepository.save(page);
    }

    public PageInfo getPageInfoById(Long id) {
        return pageInfoRepository.findById(id).orElse(null);
    }
    public Page getPageInfosBySlugAndAndTenantId(String slug, String tenant_id) {
        return pageRepository.findPageBySlugAndTenantId(slug, tenant_id);
    }

    public PageInfo updatePageInfo(PageInfoInput pageInfoInput) throws Exception {
        PageInfo pageInfo = pageInfoRepository.findPageInfoBySlugAndTenantIdAndLanguage(pageInfoInput.getSlug(), TenantContext.getCurrentProfile(), pageInfoInput.getLanguage()).orElse(null);
        if (pageInfo == null)
            throw new Exception("Page info not found");
        pageInfo.setInfo(pageInfoInput.getInfo());
        return pageInfoRepository.save(pageInfo);
    }

//    public PageInfo getPageInfoBySlugAndTenantIdAndLanguage(String slug, String tenant_id, String language) {
//        return pageInfoRepository.findPageInfoBySlugAndTenantIdAndLanguage(slug,tenant_id, language);
//    }

    public List<Page> getPagesInfosByTenantID(String tenant_id) {
        return pageRepository.findPagesByTenantId(tenant_id);
    }

    public void deleteByIdAndAndTenantId(PageInfo pageInfo, Long id, String tenant_id){
        pageInfo.getPage().getPageInfos().removeIf(page -> Objects.equals(page.getId(), id));
        pageRepository.save(pageInfo.getPage());
        pageInfoRepository.deleteByIdAndAndTenantId(id, tenant_id);
    }

    public PageInfo save(PageInfo pageInfo) {
        return pageInfoRepository.save(pageInfo);
    }

}
