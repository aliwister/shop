package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.repository.TenantStockRepository;
import com.badals.shop.service.mapper.TenantStockMapper;
import com.badals.shop.service.pojo.PartnerStock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantStockService {

    private final Logger log = LoggerFactory.getLogger(TenantStockService.class);

    private final TenantStockRepository tenantStockRepository;
    private final TenantStockMapper tenantStockMapper;

    public TenantStockService(TenantStockRepository tenantStockRepository, TenantStockMapper tenantStockMapper) {
        this.tenantStockRepository = tenantStockRepository;
        this.tenantStockMapper = tenantStockMapper;
    }

    public PartnerStock update(PartnerStock stock){
        TenantStock tenantStock = tenantStockRepository.findById(stock.getId()).orElse(null);
        if(tenantStock != null){
            tenantStock.setAvailability(stock.getAvailability());
            tenantStock.allow_backorder(stock.getAllow_backorder());
            tenantStock.availability(stock.getAvailability());
            tenantStock.cost(stock.getCost());
            tenantStock.setLocation(stock.getLocation());
            tenantStock.setQuantity(stock.getQuantity());
            tenantStock.setBackorder_availability(stock.getBackorder_availability());
            tenantStock.setStore(stock.getStore());
        }else {
            tenantStock =  tenantStockMapper.toEntity(stock);
        }

        return tenantStockMapper.toDto(tenantStockRepository.save(tenantStock));
    }
}

