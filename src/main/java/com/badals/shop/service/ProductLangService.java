package com.badals.shop.service;

import com.badals.shop.domain.ProductLang;
import com.badals.shop.repository.ProductLangRepository;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.service.mapper.ProductLangMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProductLang}.
 */
@Service
@Transactional
public class ProductLangService {

    private final Logger log = LoggerFactory.getLogger(ProductLangService.class);

    private final ProductLangRepository productLangRepository;

    private final ProductLangMapper productLangMapper;

    public ProductLangService(ProductLangRepository productLangRepository, ProductLangMapper productLangMapper) {
        this.productLangRepository = productLangRepository;
        this.productLangMapper = productLangMapper;
    }

    /**
     * Save a productLang.
     *
     * @param productLangDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductLangDTO save(ProductLangDTO productLangDTO) {
        log.debug("Request to save ProductLang : {}", productLangDTO);
        ProductLang productLang = productLangMapper.toEntity(productLangDTO);
        productLang = productLangRepository.save(productLang);
        return productLangMapper.toDto(productLang);
    }

    /**
     * Get all the productLangs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductLangDTO> findAll() {
        log.debug("Request to get all ProductLangs");
        return productLangRepository.findAll().stream()
            .map(productLangMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one productLang by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductLangDTO> findOne(Long id) {
        log.debug("Request to get ProductLang : {}", id);
        return productLangRepository.findById(id)
            .map(productLangMapper::toDto);
    }

    /**
     * Delete the productLang by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductLang : {}", id);
        productLangRepository.deleteById(id);
    }

    public ProductLangDTO addI18n(Long id, ProductLangDTO productI18n) {
        productI18n.setProductId(id);
        return this.save(productI18n);
    }
}
