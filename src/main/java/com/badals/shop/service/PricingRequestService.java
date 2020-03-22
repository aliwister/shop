package com.badals.shop.service;

import com.badals.shop.domain.PricingRequest;
import com.badals.shop.repository.PricingRequestRepository;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.mapper.PricingRequestMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final PricingRequestMapper pricingRequestMapper;

    public PricingRequestService(PricingRequestRepository pricingRequestRepository, PricingRequestMapper pricingRequestMapper) {
        this.pricingRequestRepository = pricingRequestRepository;
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

    public void push(String asin) {
        PricingRequest p = new PricingRequest();
        p.sku(asin);
        pricingRequestRepository.save(p);
    }

    public void complete(Long id) {
        PricingRequest p = pricingRequestRepository.getOne(id);
        p.setDone(true);
        pricingRequestRepository.save(p);
    }
}
