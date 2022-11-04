package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantHashtag;
import com.badals.shop.repository.SlugRepository;
import com.badals.shop.service.util.ChecksumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SlugService {

    private final Logger log = LoggerFactory.getLogger(SlugService.class);
    private final SlugRepository slugRepository;

    public SlugService(SlugRepository slugRepository) {
        this.slugRepository = slugRepository;
    }

    public Long generateRef(String sku, Long currentMerchantId) {
        String checksum = ChecksumUtil.getChecksum(sku);
        Long ref = slugRepository.getFirstUnused(Long.valueOf(checksum), System.currentTimeMillis());
        return ref;
    }
}
