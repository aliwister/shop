package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.tenant.PageInfo;
import com.badals.shop.repository.PageInfoRepository;
import com.badals.shop.service.dto.PageInfoDTO;
import com.badals.shop.service.dto.PagesInfosDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PageInfoService {
    private final Logger log = LoggerFactory.getLogger(PageInfoService.class);
    private final PageInfoRepository pageInfoRepository;

    public PageInfoService(PageInfoRepository pageInfoRepository) {
        this.pageInfoRepository = pageInfoRepository;
    }

    public PageInfo createPageInfo(PageInfoDTO pageInfoDTO) {
        PageInfo pageInfo = new PageInfo();
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

    public PageInfo updatePageInfo(PageInfoDTO pageInfoDTO) throws Exception {
        PageInfo pageInfo = pageInfoRepository.findPageInfoBySlugAndTenantIdAndLanguage(pageInfoDTO.getSlug(), TenantContext.getCurrentProfile(), pageInfoDTO.getLanguage());
        if (pageInfo == null)
            throw new Exception("Page info not found");
        pageInfo.setInfo(pageInfoDTO.getInfo());
        return pageInfoRepository.save(pageInfo);
    }

    public PageInfo getPageInfoBySlugAndTenantIdAndLanguage(String slug, String tenant_id, String language) {
        return pageInfoRepository.findPageInfoBySlugAndTenantIdAndLanguage(slug,tenant_id, language);
    }

    public List<PagesInfosDTO> getPagesInfosByTenantID(String tenant_id) {
        List<PageInfo> pageInfos = pageInfoRepository.findPageInfosByTenantId(tenant_id);
        Map<String, PagesInfosDTO> map = new HashMap<>();
        for (PageInfo pageInfo : pageInfos) {
            if (map.containsKey(pageInfo.getSlug())) {
                map.get(pageInfo.getSlug()).getPageInfos().add(pageInfo);
            } else {
                PagesInfosDTO pagesInfosDTO = new PagesInfosDTO();
                pagesInfosDTO.setSlug(pageInfo.getSlug());
                pagesInfosDTO.getPageInfos().add(pageInfo);
                map.put(pageInfo.getSlug(), pagesInfosDTO);
            }
        }
        return new ArrayList<>(map.values());
    }

    public void deleteById(Long id) {
        pageInfoRepository.deleteById(id);
    }

    public PageInfo save(PageInfo pageInfo) {
        return pageInfoRepository.save(pageInfo);
    }

}
