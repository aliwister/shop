package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantFaqCategory;
import com.badals.shop.domain.tenant.TenantFaqCategoryName;
import com.badals.shop.repository.FaqCategoryRepository;
import com.badals.shop.service.dto.FaqCategoryNameInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FaqCategoryService {
    private final Logger log = LoggerFactory.getLogger(FaqCategoryService.class);
    private final FaqCategoryRepository faqCategoryRepository;

    public FaqCategoryService(FaqCategoryRepository faqCategoryRepository) {
        this.faqCategoryRepository = faqCategoryRepository;
    }

    public List<TenantFaqCategory> getFaqCategories() {
        return faqCategoryRepository.findAll();
    }

    public TenantFaqCategory addCategory(FaqCategoryNameInput faqCategoryNameInput){
        TenantFaqCategory tenantFaqCategory = faqCategoryRepository.findTenantFaqCategoryByPosition(faqCategoryNameInput.getPosition());
        if (tenantFaqCategory == null){
            tenantFaqCategory = new TenantFaqCategory();
            tenantFaqCategory.setPosition(faqCategoryNameInput.getPosition());
            faqCategoryRepository.save(tenantFaqCategory);
        }
        TenantFaqCategoryName tenantFaqCategoryName = new TenantFaqCategoryName();
        tenantFaqCategoryName.setFaqCategory(tenantFaqCategory);
        tenantFaqCategoryName.setLanguage(faqCategoryNameInput.getLanguage());
        tenantFaqCategoryName.setName(faqCategoryNameInput.getName());
        tenantFaqCategory.getFaqCategoryNames().add(tenantFaqCategoryName);
        return faqCategoryRepository.save(tenantFaqCategory);
    }

}
