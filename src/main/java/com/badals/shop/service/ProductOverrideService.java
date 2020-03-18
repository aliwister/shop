package com.badals.shop.service;

import com.badals.shop.domain.ProductOverride;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.service.dto.ProductOverrideDTO;
import com.badals.shop.service.mapper.ProductOverrideMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link ProductOverride}.
 */
@Service
@Transactional
public class ProductOverrideService {

    private final Logger log = LoggerFactory.getLogger(ProductOverrideService.class);

    private final ProductOverrideRepository productOverrideRepository;

    private final ProductOverrideMapper productOverrideMapper;

    public ProductOverrideService(ProductOverrideRepository productOverrideRepository, ProductOverrideMapper productOverrideMapper) {
        this.productOverrideRepository = productOverrideRepository;
        this.productOverrideMapper = productOverrideMapper;
    }

    /**
     * Save a productOverride.
     *
     * @param productOverrideDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductOverrideDTO save(ProductOverrideDTO productOverrideDTO) {
        log.debug("Request to save ProductOverride : {}", productOverrideDTO);
        ProductOverride productOverride = productOverrideMapper.toEntity(productOverrideDTO);
        productOverride = productOverrideRepository.save(productOverride);
        return productOverrideMapper.toDto(productOverride);
    }

    /**
     * Get all the productOverrides.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductOverrideDTO> findAll() {
        log.debug("Request to get all ProductOverrides");
        return productOverrideRepository.findAll().stream()
            .map(productOverrideMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one productOverride by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductOverrideDTO> findOne(Long id) {
        log.debug("Request to get ProductOverride : {}", id);
        return productOverrideRepository.findById(id)
            .map(productOverrideMapper::toDto);
    }

    /**
     * Delete the productOverride by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete ProductOverride : {}", id);
        productOverrideRepository.deleteById(id);
    }
}
