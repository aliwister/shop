package com.badals.shop.service;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.service.dto.TenantDTO;
import com.badals.shop.service.mapper.TenantMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Tenant}.
 */
@Service
@Transactional
public class TenantLayoutService {

    private final Logger log = LoggerFactory.getLogger(TenantLayoutService.class);

    private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    public TenantLayoutService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    public TenantDTO getTenant() {
        Tenant tenant = tenantRepository.findAll().get(0);
        return tenantMapper.toDto(tenant);
    }

    public List<Attribute> getSliders() {
        Locale locale = LocaleContextHolder.getLocale();
        String country = locale.getCountry();
        String language = locale.getLanguage();

        Tenant tenant = tenantRepository.findAll().get(0);
        if (tenant.getSliders() != null) {
            List<String> images = tenant.getSliders().getMap().get(language+"-"+country);
            if (images == null)
                images = tenant.getSliders().getMap().get(language);
            if(images == null && tenant.getDefaultLocale() != null)
                images = tenant.getSliders().getMap().get(tenant.getDefaultLocale());
            if(images != null )
                return images.stream().map(y->new Attribute("slider", y)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public List<Attribute> getSocial() {
        Locale locale = LocaleContextHolder.getLocale();
        Tenant tenant = tenantRepository.findAll().get(0);
        if (tenant.getSocialProfile() != null) {
            final Map<?, String> profiles;
            if (tenant.getSocialProfile().getMap().get(locale.toString()) != null)
                profiles =tenant.getSocialProfile().getMap().get(locale.toString());
            else if(tenant.getDefaultLocale() != null)
                profiles = tenant.getSocialProfile().getMap().get(tenant.getDefaultLocale());
            else
                profiles = null;
            if(profiles != null )
                return profiles.keySet().stream().map(x -> new Attribute(x.toString(), profiles.get(x))).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}
