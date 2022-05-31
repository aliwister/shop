package com.badals.shop.service;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.SliderConfig;
import com.badals.shop.domain.tenant.Media;
import com.badals.shop.domain.tenant.S3UploadRequest;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.repository.MediaRepository;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.service.dto.TenantDTO;
import com.badals.shop.service.mapper.TenantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Tenant}.
 */
@Service
@Transactional
public class TenantSetupService {

    private final Logger log = LoggerFactory.getLogger(TenantSetupService.class);

    private final TenantRepository tenantRepository;
    private final MediaRepository mediaRepository;

    private final TenantMapper tenantMapper;

    public TenantSetupService(TenantRepository tenantRepository, MediaRepository mediaRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.mediaRepository = mediaRepository;
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
        return tenantRepository.findAll().stream()
            .map(tenantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get all the tenants with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
/*    public Page<TenantDTO> findAllWithEagerRelationships(Pageable pageable) {
        return tenantRepository.findAllWithEagerRelationships(pageable).map(tenantMapper::toDto);
    }*/
    

    /**
     * Get one tenant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
/*    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findOneWithEagerRelationships(id)
            .map(tenantMapper::toDto);
    }*/
    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(Long id) {
        log.debug("Request to get Tenant : {}", id);
        return tenantRepository.findById(id)
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

   public void updateLogo(S3UploadRequest request) {
        Tenant tenant = tenantRepository.findAll().get(0);
        tenant.setLogo(request.getUrl());
        tenantRepository.save(tenant);
   }

    public void addMedia(S3UploadRequest request) {
        Media media = new Media();
        media.setKey(request.getKey());
        media.setUrl(request.getUrl());
        mediaRepository.save(media);
    }

    public List<Attribute> getSliders(String locale) {
        Tenant tenant = tenantRepository.findAll().get(0);
        if (tenant.getSliders() != null) {
            List<String> images = tenant.getSliders().getMap().get(locale);
            if(images != null )
                return images.stream().map(y->new Attribute("slider", y)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
    @Transactional
    public void setSliders(String locale, List<String> images) {
        Tenant tenant = tenantRepository.findAll().get(0);
        SliderConfig config = tenant.getSliders();
        if (config == null)
            config = new SliderConfig();

        config.getMap().put(locale, images);
        tenant.setSliders(config);
        tenantRepository.save(tenant);
    }
}
