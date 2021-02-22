package com.badals.shop.service;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.Recycle;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.RecycleRepository;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.ChildProduct;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.zip.CRC32;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class RecycleService {

    private final Logger log = LoggerFactory.getLogger(RecycleService.class);
    private final ProductRepository productRepository;
    private final RecycleRepository recycleRepository;

    public RecycleService(ProductRepository productRepository, RecycleRepository recycleRepository) {
        this.productRepository = productRepository;
        this.recycleRepository = recycleRepository;
    }


    public void recycleS3(String type, String key) {
        Recycle s3 = new Recycle();
        s3.setType(type);
        s3.setKey(key);
        recycleRepository.save(s3);
    }

}
