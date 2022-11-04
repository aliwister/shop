package com.badals.shop.service;


import com.badals.shop.domain.Recycle;

import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.repository.RecycleRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * Service Implementation for managing {@link TenantProduct}.
 */
@Service
@Transactional
public class RecycleService {

    private final Logger log = LoggerFactory.getLogger(RecycleService.class);
    private final RecycleRepository recycleRepository;

    public RecycleService(RecycleRepository recycleRepository) {
        this.recycleRepository = recycleRepository;
    }


    public void recycleS3(String type, String key) {
        Recycle s3 = new Recycle();
        s3.setType(type);
        s3.setKey(key);
        recycleRepository.save(s3);
    }

}
