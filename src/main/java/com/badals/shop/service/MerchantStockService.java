package com.badals.shop.service;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.repository.MerchantStockRepository;
import com.badals.shop.service.dto.MerchantStockDTO;
import com.badals.shop.service.mapper.MerchantStockMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link MerchantStock}.
 */
@Service
@Transactional
public class MerchantStockService {

    private final Logger log = LoggerFactory.getLogger(MerchantStockService.class);

    private final MerchantStockRepository merchantStockRepository;

    private final MerchantStockMapper merchantStockMapper;

    public MerchantStockService(MerchantStockRepository merchantStockRepository, MerchantStockMapper merchantStockMapper) {
        this.merchantStockRepository = merchantStockRepository;
        this.merchantStockMapper = merchantStockMapper;
    }

    /**
     * Save a merchantStock.
     *
     * @param merchantStockDTO the entity to save.
     * @return the persisted entity.
     */
    public MerchantStockDTO save(MerchantStockDTO merchantStockDTO) {
        log.debug("Request to save MerchantStock : {}", merchantStockDTO);
        MerchantStock merchantStock = merchantStockMapper.toEntity(merchantStockDTO);
        merchantStock = merchantStockRepository.save(merchantStock);
        return merchantStockMapper.toDto(merchantStock);
    }

    /**
     * Get all the merchantStocks.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MerchantStockDTO> findAll() {
        log.debug("Request to get all MerchantStocks");
        return merchantStockRepository.findAll().stream()
            .map(merchantStockMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one merchantStock by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MerchantStockDTO> findOne(Long id) {
        log.debug("Request to get MerchantStock : {}", id);
        return merchantStockRepository.findById(id)
            .map(merchantStockMapper::toDto);
    }

    /**
     * Delete the merchantStock by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete MerchantStock : {}", id);
        merchantStockRepository.deleteById(id);
    }
}
