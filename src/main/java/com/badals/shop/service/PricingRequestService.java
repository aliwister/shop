package com.badals.shop.service;

import com.badals.shop.domain.PricingRequest;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.repository.PricingRequestRepository;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.mapper.PricingRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PricingRequest}.
 */
@Service
@Transactional
public class PricingRequestService {

    private final Logger log = LoggerFactory.getLogger(PricingRequestService.class);

    private final PricingRequestRepository pricingRequestRepository;
    private final ProductOverrideRepository productOverrideRepository;

    private final PricingRequestMapper pricingRequestMapper;

    public PricingRequestService(PricingRequestRepository pricingRequestRepository, ProductOverrideRepository productOverrideRepository, PricingRequestMapper pricingRequestMapper) {
        this.pricingRequestRepository = pricingRequestRepository;
        this.productOverrideRepository = productOverrideRepository;
        this.pricingRequestMapper = pricingRequestMapper;
    }

    /**
     * Save a pricingRequest.
     *
     * @param pricingRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public PricingRequestDTO save(PricingRequestDTO pricingRequestDTO) {
        log.debug("Request to save PricingRequest : {}", pricingRequestDTO);
        PricingRequest pricingRequest = pricingRequestMapper.toEntity(pricingRequestDTO);
        pricingRequest = pricingRequestRepository.save(pricingRequest);
        return pricingRequestMapper.toDto(pricingRequest);
    }

    /**
     * Get all the pricingRequests.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PricingRequestDTO> findAll() {
        log.debug("Request to get all PricingRequests");
        return pricingRequestRepository.findAll().stream()
            .map(pricingRequestMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one pricingRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PricingRequestDTO> findOne(Long id) {
        log.debug("Request to get PricingRequest : {}", id);
        return pricingRequestRepository.findById(id)
            .map(pricingRequestMapper::toDto);
    }

    /**
     * Delete the pricingRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PricingRequest : {}", id);
        pricingRequestRepository.deleteById(id);
    }

    public List<PricingRequestDTO> findUnprocessed() {
        return pricingRequestRepository.findWithProduct().stream().map(pricingRequestMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    public void push(String asin, String email) throws RuntimeException {
        if(pricingRequestRepository.existsBySkuAndCreatedBy(asin,email))
            throw new RuntimeException("Request already exists and we are still processing it");
        PricingRequest p = new PricingRequest();
        p.sku(asin);
        p.setDone(null);
        p.setEmailSent(null);
        pricingRequestRepository.save(p);
    }

    public PricingRequestDTO complete(Long id) {
        PricingRequest p = pricingRequestRepository.getOne(id);
        p.setDone(true);
        return pricingRequestMapper.toDto(pricingRequestRepository.save(p));
    }

    public List<PricingRequestDTO> findAllByCreatedByAndDone(String email, boolean done) {
        return pricingRequestRepository.findAllByCreatedByAndDoneAndEmailSentIsNull(email, done).stream()
                .map(pricingRequestMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void setEmailSent(List<PricingRequestDTO> dtos) {
        List<Long> list = dtos.stream().map(PricingRequestDTO::getId).collect(Collectors.toList());
        if(!list.isEmpty())
            pricingRequestRepository.updateEmailSentWhereIdIn(list);
    }

    public List<ProductOverride> findOverrides(String asin, String parent) {
        if (parent == null)
            return productOverrideRepository.findBySku(asin);
        return productOverrideRepository.findBySkuIn(Arrays.asList(new String[]{asin, parent}));
    }

}
