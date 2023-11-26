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
public class FaqPublicService {
    private final Logger log = LoggerFactory.getLogger(FaqPublicService.class);

    private final FaqQARepository faqQARepository;
    private final FaqCategoryRepository faqCategoryRepository;;


    public FaqPublicService(FaqQARepository faqQARepository, FaqCategoryRepository faqCategoryRepository) {
        this.faqQARepository = faqQARepository;
        this.faqCategoryRepository = faqCategoryRepository;
    }


    public List<TenantFaqQA> getFaqQAs(String tenant_id) {
        return faqQARepository.findAllByTenantIdAndEnabled(tenant_id, true);
    }
    public List<TenantFaqCategory> getFaqCategories(String tenant_id) {
        return faqCategoryRepository.findAllByTenantIdAndEnabled(tenant_id, true);
    }


}
