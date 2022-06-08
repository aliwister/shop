package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.SliderConfig;
import com.badals.shop.domain.pojo.SocialPlatform;
import com.badals.shop.domain.pojo.SocialProfile;
import com.badals.shop.domain.tenant.Media;
import com.badals.shop.domain.tenant.S3UploadRequest;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.domain.tenant.TenantHashtag;
import com.badals.shop.repository.MediaRepository;
import com.badals.shop.repository.TenantHashtagRepository;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import com.badals.shop.service.dto.TenantDTO;
import com.badals.shop.service.mapper.TenantHashtagMapper;
import com.badals.shop.service.mapper.TenantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
   private final TenantHashtagRepository hashtagRepository;
   private final TenantHashtagMapper hashtagMapper;
   public TenantSetupService(TenantRepository tenantRepository, MediaRepository mediaRepository, TenantMapper tenantMapper, TenantHashtagRepository hashtagRepository, TenantHashtagMapper hashtagMapper) {
      this.tenantRepository = tenantRepository;
      this.mediaRepository = mediaRepository;
      this.tenantMapper = tenantMapper;
      this.hashtagRepository = hashtagRepository;
      this.hashtagMapper = hashtagMapper;
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
         if (images != null)
            return images.stream().map(y -> new Attribute("slider", y)).collect(Collectors.toList());
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

   @Transactional
   public void setSocialProfile(String locale, List<Attribute> profiles) {
      Tenant tenant = tenantRepository.findAll().get(0);
      SocialProfile config = tenant.getSocialProfile();
      if (config == null)
         config = new SocialProfile();

      Map<SocialPlatform, String> profile = profiles.stream().collect(Collectors.toMap(x -> SocialPlatform.valueOf(x.getName()), Attribute::getValue));
      config.getMap().put(locale, profile);
      tenant.setSocialProfile(config);
      tenantRepository.save(tenant);
   }

   @Transactional
   public ProfileHashtagDTO saveTag(ProfileHashtagDTO dto) throws NoSuchFieldException {
      TenantHashtag tag = null;
      if(dto.getId() != null)
         tag = hashtagRepository.findById(dto.getId()).orElseThrow(() -> new NoSuchFieldException("Invalid tag id"));

      if(tag == null)
         tag = hashtagMapper.toEntity(dto);
      else {
         tag.setIcon(dto.getIcon());
         tag.setName(dto.getName());
         tag.setLangs(dto.getLangs());
         tag.setPosition(dto.getPosition());
      }
      tag = hashtagRepository.save(tag);
      return hashtagMapper.toDto(tag);
   }

   @Transactional
   public void deleteTag(Long id) {
      //tag.setTenantId(TenantContext.getCurrentTenant());
      hashtagRepository.deleteById(id);
   }
   public List<ProfileHashtagDTO> tenantTags() {
      String profile = TenantContext.getCurrentProfile();
      return hashtagRepository.findForList(profile).stream().map(hashtagMapper::toDto).collect(Collectors.toList());
   }

   public List<Attribute> getSocial(String locale) {
      Tenant tenant = tenantRepository.findAll().get(0);
      if (tenant.getSocialProfile() != null) {
         Map<SocialPlatform, String> social = tenant.getSocialProfile().getMap().get(locale);
         if (social != null)
            return social.keySet().stream().map(y -> new Attribute(y.name(), social.get(y))).collect(Collectors.toList());
      }
      return Collections.emptyList();
   }
}
