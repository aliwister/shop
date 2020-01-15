package com.badals.shop.service;

import com.badals.shop.domain.Carrier;
import com.badals.shop.repository.CarrierRepository;
import com.badals.shop.service.dto.CarrierDTO;
import com.badals.shop.service.mapper.CarrierMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Carrier}.
 */
@Service
@Transactional
public class CarrierService {

    private final Logger log = LoggerFactory.getLogger(CarrierService.class);

    private final CarrierRepository carrierRepository;

    private final CarrierMapper carrierMapper;

    public CarrierService(CarrierRepository carrierRepository, CarrierMapper carrierMapper) {
        this.carrierRepository = carrierRepository;
        this.carrierMapper = carrierMapper;
    }

    /**
     * Save a carrier.
     *
     * @param carrierDTO the entity to save.
     * @return the persisted entity.
     */
    public CarrierDTO save(CarrierDTO carrierDTO) {
        log.debug("Request to save Carrier : {}", carrierDTO);
        Carrier carrier = carrierMapper.toEntity(carrierDTO);
        carrier = carrierRepository.save(carrier);
        return carrierMapper.toDto(carrier);
    }

    /**
     * Get all the carriers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CarrierDTO> findAll() {
        log.debug("Request to get all Carriers");
        return carrierRepository.findAll().stream()
            .map(carrierMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one carrier by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CarrierDTO> findOne(Long id) {
        log.debug("Request to get Carrier : {}", id);
        return carrierRepository.findById(id)
            .map(carrierMapper::toDto);
    }

    /**
     * Delete the carrier by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Carrier : {}", id);
        carrierRepository.deleteById(id);
    }
}
