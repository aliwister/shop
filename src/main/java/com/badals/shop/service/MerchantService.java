package com.badals.shop.service;

import com.badals.shop.domain.Merchant;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.service.dto.MerchantDTO;
import com.badals.shop.service.mapper.MerchantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Merchant}.
 */
@Service
@Transactional
public class MerchantService {

    private final Logger log = LoggerFactory.getLogger(MerchantService.class);

    private final MerchantRepository merchantRepository;

    private final MerchantMapper merchantMapper;

    public MerchantService(MerchantRepository merchantRepository, MerchantMapper merchantMapper) {
        this.merchantRepository = merchantRepository;
        this.merchantMapper = merchantMapper;
    }

    /**
     * Save a merchant.
     *
     * @param merchantDTO the entity to save.
     * @return the persisted entity.
     */
    public MerchantDTO save(MerchantDTO merchantDTO) {
        log.debug("Request to save Merchant : {}", merchantDTO);
        Merchant merchant = merchantMapper.toEntity(merchantDTO);
        merchant = merchantRepository.save(merchant);
        return merchantMapper.toDto(merchant);
    }

    /**
     * Get all the merchants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<MerchantDTO> findAll() {
        log.debug("Request to get all Merchants");
        return merchantRepository.findAll().stream()
            .map(merchantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one merchant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<MerchantDTO> findOne(Long id) {
        log.debug("Request to get Merchant : {}", id);
        return merchantRepository.findById(id)
            .map(merchantMapper::toDto);
    }

    /**
     * Delete the merchant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Merchant : {}", id);
        merchantRepository.deleteById(id);
    }

    public MerchantDTO merchantByDomain(String domain) {
        return merchantRepository.findMerchantByDomain(domain).map(merchantMapper::toDto).orElse(null);
    }
}
