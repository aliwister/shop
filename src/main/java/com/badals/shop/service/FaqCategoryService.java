package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantFaqCategory;
import com.badals.shop.domain.pojo.TenantFaqCategoryName;
import com.badals.shop.repository.FaqCategoryRepository;
import com.badals.shop.service.dto.FaqCategoryNameInput;
import com.badals.shop.service.dto.FaqDeleteInput;
import com.badals.shop.service.pojo.Message;
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

    public TenantFaqCategory getFaqCategoryById(Long id) {
        return faqCategoryRepository.findById(id).orElse(null);
    }

    public TenantFaqCategory addCategory(FaqCategoryNameInput faqCategoryNameInput){
        TenantFaqCategory tenantFaqCategory = faqCategoryRepository.findTenantFaqCategoryByPosition(faqCategoryNameInput.getPosition());
        if (tenantFaqCategory == null){
            tenantFaqCategory = new TenantFaqCategory();
            tenantFaqCategory.setPosition(faqCategoryNameInput.getPosition());
            faqCategoryRepository.save(tenantFaqCategory);
        }
        TenantFaqCategoryName tenantFaqCategoryName = new TenantFaqCategoryName();
        tenantFaqCategoryName.setLanguage(faqCategoryNameInput.getLanguage());
        tenantFaqCategoryName.setName(faqCategoryNameInput.getName());
        tenantFaqCategory.getFaqCategoryNames().add(tenantFaqCategoryName);
        return faqCategoryRepository.save(tenantFaqCategory);
    }

    public TenantFaqCategory updateCategory(FaqCategoryNameInput faqCategoryNameInput){
        TenantFaqCategory tenantFaqCategory = faqCategoryRepository.findTenantFaqCategoryByPosition(faqCategoryNameInput.getPosition());
        if (tenantFaqCategory == null)
            throw new RuntimeException("Category not found");
        TenantFaqCategoryName tenantFaqCategoryName = tenantFaqCategory.getFaqCategoryNames().stream().filter(tenantFaqCategoryName1 -> tenantFaqCategoryName1.getLanguage().equals(faqCategoryNameInput.getLanguage())).findFirst().orElse(null);
        if (tenantFaqCategoryName == null)
            throw new RuntimeException("Category with the given language not found");
        tenantFaqCategoryName.setName(faqCategoryNameInput.getName());
        return faqCategoryRepository.save(tenantFaqCategory);
    }

    public Message deleteCategory(FaqDeleteInput faqDeleteInput){
        TenantFaqCategory tenantFaqCategory = faqCategoryRepository.findById(faqDeleteInput.getId()).orElse(null);
        if (tenantFaqCategory == null)
            throw new RuntimeException("Category not found");
        if(faqDeleteInput.getLanguage() == null){
            faqCategoryRepository.delete(tenantFaqCategory);
            return new Message("Category deleted successfully");
        }
        TenantFaqCategoryName tenantFaqCategoryName = tenantFaqCategory.getFaqCategoryNames().stream().filter(tenantFaqCategoryName1 -> tenantFaqCategoryName1.getLanguage().equals(faqDeleteInput.getLanguage())).findFirst().orElse(null);
        if (tenantFaqCategoryName == null)
            throw new RuntimeException("Category with the given language not found");
        tenantFaqCategory.getFaqCategoryNames().remove(tenantFaqCategoryName);
        faqCategoryRepository.save(tenantFaqCategory);
        return new Message("Category deleted successfully");
    }

}
