package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantFaqQA;
import com.badals.shop.domain.tenant.TenantFaqQALanguage;
import com.badals.shop.repository.FaqQARepository;
import com.badals.shop.service.dto.FaqQAInput;
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

    public FaqQAService(FaqQARepository faqQARepository){
        this.faqQARepository = faqQARepository;
    }
    public List<TenantFaqQA> getFaqQAs() {
        return faqQARepository.findAll();
    }

    public TenantFaqQA addQA(FaqQAInput faqQAInput) {
        TenantFaqQA tenantFaqQA = faqQARepository.findTenantFaqQAByCategoryIdAndPosition(faqQAInput.getCategoryId(), faqQAInput.getPosition());
        if(tenantFaqQA == null){
            tenantFaqQA = new TenantFaqQA();
            tenantFaqQA.setCategoryId(faqQAInput.getCategoryId());
            tenantFaqQA.setPosition(faqQAInput.getPosition());
            tenantFaqQA = faqQARepository.save(tenantFaqQA);
        }
        TenantFaqQALanguage tenantFaqQALanguage = new TenantFaqQALanguage();
        tenantFaqQALanguage.setFaqQA(tenantFaqQA);
        tenantFaqQALanguage.setLanguage(faqQAInput.getLanguage());
        tenantFaqQALanguage.setQuestion(faqQAInput.getQuestion());
        tenantFaqQALanguage.setAnswer(faqQAInput.getAnswer());
        tenantFaqQA.getFaqQALanguages().add(tenantFaqQALanguage);
        return faqQARepository.save(tenantFaqQA);

    }
}
