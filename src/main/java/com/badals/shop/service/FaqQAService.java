package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantFaqCategory;
import com.badals.shop.domain.tenant.TenantFaqQA;
import com.badals.shop.domain.pojo.TenantFaqQALanguage;
import com.badals.shop.repository.FaqCategoryRepository;
import com.badals.shop.repository.FaqQARepository;
import com.badals.shop.service.dto.FaqDeleteInput;
import com.badals.shop.service.dto.FaqQAInput;
import com.badals.shop.service.pojo.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FaqQAService {
    private final Logger log = LoggerFactory.getLogger(FaqQAService.class);

    private final FaqQARepository faqQARepository;
    private final FaqCategoryRepository faqCategoryRepository;;

    public FaqQAService(FaqQARepository faqQARepository, FaqCategoryRepository faqCategoryRepository){
        this.faqQARepository = faqQARepository;
        this.faqCategoryRepository = faqCategoryRepository;
    }
    public List<TenantFaqQA> getFaqQAs() {
        return faqQARepository.findAll();
    }

    public TenantFaqQA addQA(FaqQAInput faqQAInput) {
        TenantFaqQA tenantFaqQA = faqQARepository.findTenantFaqQAByCategoryIdAndPosition(faqQAInput.getCategoryId(), faqQAInput.getPosition());
        if(tenantFaqQA == null){
            tenantFaqQA = new TenantFaqQA();
            TenantFaqCategory tenantFaqCategory = faqCategoryRepository.findById(faqQAInput.getCategoryId()).orElse(null);
            if (tenantFaqCategory == null)
                throw new RuntimeException("Category not found");
            tenantFaqQA.setTenantFaqCategory(tenantFaqCategory);
            tenantFaqQA.setCategoryId(faqQAInput.getCategoryId());
            tenantFaqQA.setPosition(faqQAInput.getPosition());
            tenantFaqQA = faqQARepository.save(tenantFaqQA);
        }
        TenantFaqQALanguage tenantFaqQALanguage = new TenantFaqQALanguage();
        tenantFaqQALanguage.setLanguage(faqQAInput.getLanguage());
        tenantFaqQALanguage.setQuestion(faqQAInput.getQuestion());
        tenantFaqQALanguage.setAnswer(faqQAInput.getAnswer());
        tenantFaqQA.getFaqQALanguages().add(tenantFaqQALanguage);
        return faqQARepository.save(tenantFaqQA);
    }

    public TenantFaqQA updateQA(FaqQAInput faqQAInput) {
        TenantFaqQA tenantFaqQA = faqQARepository.findTenantFaqQAByCategoryIdAndPosition(faqQAInput.getCategoryId(), faqQAInput.getPosition());
        if(tenantFaqQA == null)
            throw new RuntimeException("QA not found");
        TenantFaqQALanguage tenantFaqQALanguage = tenantFaqQA.getFaqQALanguages().stream().filter(tenantFaqQALanguage1 -> tenantFaqQALanguage1.getLanguage().equals(faqQAInput.getLanguage())).findFirst().orElse(null);
        if(tenantFaqQALanguage == null)
            throw new RuntimeException("QA with the given language not found");
        tenantFaqQALanguage.setQuestion(faqQAInput.getQuestion());
        tenantFaqQALanguage.setAnswer(faqQAInput.getAnswer());
        return faqQARepository.save(tenantFaqQA);

    }

    public Message deleteQA(FaqDeleteInput faqDeleteInput){
        TenantFaqQA tenantFaqQA = faqQARepository.findById(faqDeleteInput.getId()).orElse(null);
        if(tenantFaqQA == null)
            throw new RuntimeException("QA not found");
        if(faqDeleteInput.getLanguage()==null || faqDeleteInput.getLanguage().isEmpty()){
            faqQARepository.delete(tenantFaqQA);
            return new Message("QA deleted successfully");
        }
        TenantFaqQALanguage tenantFaqQALanguage = tenantFaqQA.getFaqQALanguages().stream().filter(tenantFaqQALanguage1 -> tenantFaqQALanguage1.getLanguage().equals(faqDeleteInput.getLanguage())).findFirst().orElse(null);
        if(tenantFaqQALanguage == null)
            throw new RuntimeException("QA with the given language not found");
        tenantFaqQA.getFaqQALanguages().remove(tenantFaqQALanguage);
        faqQARepository.save(tenantFaqQA);
        return new Message("QA deleted successfully");
    }
}
