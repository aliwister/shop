package com.badals.shop.service;

import com.badals.shop.domain.tenant.PageInfo;
import com.badals.shop.repository.PageInfoRepository;
import com.badals.shop.service.dto.PageInfoDTO;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class PageInfoService {
    private final Logger log = LoggerFactory.getLogger(PageInfoService.class);
    private final PageInfoRepository pageInfoRepository;

    public PageInfoService(PageInfoRepository pageInfoRepository) {
        this.pageInfoRepository = pageInfoRepository;
    }

    public PageInfo createPageInfo(String tenant_id, PageInfoDTO pageInfoDTO) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.setTenantId(tenant_id);
        pageInfo.setLanguage(pageInfoDTO.getLanguage());
        pageInfo.setSlug(pageInfoDTO.getSlug());
        pageInfo.setInfo(pageInfoDTO.getInfo());
        return pageInfoRepository.save(pageInfo);
    }

    public PageInfo getPageInfoById(Long id) {
        return pageInfoRepository.findById(id).orElse(null);
    }
    public List<PageInfo> getPageInfosBySlugAndAndTenantId(String slug, String tenant_id) {
        return pageInfoRepository.findPageInfosBySlugAndTenantId(slug, tenant_id);
    }

    public PageInfo updatePageInfo(PageInfoDTO pageInfoDTO, String tenant_id) throws Exception {
        PageInfo pageInfo = pageInfoRepository.findPageInfoBySlugAndTenantIdAndLanguage(pageInfoDTO.getSlug(), tenant_id, pageInfoDTO.getLanguage());
        if (pageInfo == null)
            throw new Exception("Page info not found");
        pageInfo.setInfo(pageInfoDTO.getInfo());
        return pageInfoRepository.save(pageInfo);
    }

    public PageInfo getPageInfoBySlugAndTenantIdAndLanguage(String slug, String tenant_id, String language) {
        return pageInfoRepository.findPageInfoBySlugAndTenantIdAndLanguage(slug,tenant_id, language);
    }

    public void deleteById(Long id) {
        pageInfoRepository.deleteById(id);
    }

    public PageInfo save(PageInfo pageInfo) {
        return pageInfoRepository.save(pageInfo);
    }

}
