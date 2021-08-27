package com.badals.shop.service;

import com.badals.shop.domain.Tenant;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.service.dto.TenantDTO;
import com.badals.shop.service.mapper.TenantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Tenant}.
 */
@Service
@Transactional
public class TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantService.class);

    private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    /**
     * Save a tenant.
     *
     * @param tenantDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantDTO save(TenantDTO tenantDTO) {
        log.debug("Request to save Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    /**
     * Get all the tenants.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TenantDTO> findAll() {
        log.debug("Request to get all Tenants");
        return tenantRepository.findAllWithEagerRelationships().stream()
            .map(tenantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the tenants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<TenantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tenantRepository.findAllWithEagerRelationships(pageable).map(tenantMapper::toDto);
    }
    

    /**
     * Get one tenant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findOneWithEagerRelationships(id)
            .map(tenantMapper::toDto);
    }

    /**
     * Delete the tenant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Tenant : {}", id);
        tenantRepository.deleteById(id);
    }

    public TenantDTO findOneByName(String name) {
        return tenantRepository.findByNameIgnoreCase(name).map(tenantMapper::toDto).orElse(null);
    }
}
