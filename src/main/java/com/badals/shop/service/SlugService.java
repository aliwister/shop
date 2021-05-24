package com.badals.shop.service;

import com.badals.shop.domain.Hashtag;
import com.badals.shop.graph.HashtagResponse;
import com.badals.shop.repository.HashtagRepository;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.mapper.HashtagMapper;
import com.badals.shop.service.util.ChecksumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Hashtag}.
 */
@Service
@Transactional
public class SlugService {

    private final Logger log = LoggerFactory.getLogger(SlugService.class);

    /*Long generateSlug(String sku, String merchantId) {

    }*/

    public String generateRef(String sku, Long currentMerchantId) {
        return currentMerchantId.toString() + String.valueOf(ChecksumUtil.getChecksum(sku));
    }
}
