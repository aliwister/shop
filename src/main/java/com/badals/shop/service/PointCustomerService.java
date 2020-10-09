package com.badals.shop.service;

import com.badals.shop.domain.PointCustomer;
import com.badals.shop.repository.PointCustomerRepository;
import com.badals.shop.service.dto.PointCustomerDTO;
import com.badals.shop.service.mapper.PointCustomerMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link PointCustomer}.
 */
@Service
@Transactional
public class PointCustomerService {

    private final Logger log = LoggerFactory.getLogger(PointCustomerService.class);

    private final PointCustomerRepository pointCustomerRepository;

    private final PointCustomerMapper pointCustomerMapper;

    public PointCustomerService(PointCustomerRepository pointCustomerRepository, PointCustomerMapper pointCustomerMapper) {
        this.pointCustomerRepository = pointCustomerRepository;
        this.pointCustomerMapper = pointCustomerMapper;
    }

    /**
     * Save a pointCustomer.
     *
     * @param pointCustomerDTO the entity to save.
     * @return the persisted entity.
     */
    public PointCustomerDTO save(PointCustomerDTO pointCustomerDTO) {
        log.debug("Request to save PointCustomer : {}", pointCustomerDTO);
        PointCustomer pointCustomer = pointCustomerMapper.toEntity(pointCustomerDTO);
        pointCustomer = pointCustomerRepository.save(pointCustomer);
        return pointCustomerMapper.toDto(pointCustomer);
    }

    /**
     * Get all the pointCustomers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PointCustomerDTO> findAll() {
        log.debug("Request to get all PointCustomers");
        return pointCustomerRepository.findAll().stream()
            .map(pointCustomerMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one pointCustomer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PointCustomerDTO> findOne(Long id) {
        log.debug("Request to get PointCustomer : {}", id);
        return pointCustomerRepository.findById(id)
            .map(pointCustomerMapper::toDto);
    }

    /**
     * Delete the pointCustomer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PointCustomer : {}", id);
        pointCustomerRepository.deleteById(id);
    }
}
