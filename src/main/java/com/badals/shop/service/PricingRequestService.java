package com.badals.shop.service;

import com.badals.shop.domain.PricingRequest;
import com.badals.shop.repository.PricingRequestRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PricingRequest}.
 */
@Service
@Transactional
public class PricingRequestService {

    private final Logger log = LoggerFactory.getLogger(PricingRequestService.class);

    private final PricingRequestRepository pricingRequestRepository;

    public PricingRequestService(PricingRequestRepository pricingRequestRepository) {
        this.pricingRequestRepository = pricingRequestRepository;
    }

    /**
     * Save a pricingRequest.
     *
     * @param pricingRequest the entity to save.
     * @return the persisted entity.
     */
    public PricingRequest save(PricingRequest pricingRequest) {
        log.debug("Request to save PricingRequest : {}", pricingRequest);
        return pricingRequestRepository.save(pricingRequest);
    }

    /**
     * Get all the pricingRequests.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PricingRequest> findAll() {
        log.debug("Request to get all PricingRequests");
        return pricingRequestRepository.findAll();
    }


    /**
     * Get one pricingRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PricingRequest> findOne(Long id) {
        log.debug("Request to get PricingRequest : {}", id);
        return pricingRequestRepository.findById(id);
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
}
