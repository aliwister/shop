package com.badals.shop.service;

import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.TenantStockRepository;
import com.badals.shop.service.mapper.TenantStockMapper;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.pojo.PartnerStock;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TenantStockService {

    private final Logger log = LoggerFactory.getLogger(TenantStockService.class);

    private final TenantStockRepository tenantStockRepository;
    private final TenantStockMapper tenantStockMapper;
    private final TenantProductService productService;

    public TenantStockService(TenantStockRepository tenantStockRepository, TenantStockMapper tenantStockMapper, TenantProductService productService) {
        this.tenantStockRepository = tenantStockRepository;
        this.tenantStockMapper = tenantStockMapper;
        this.productService = productService;
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_MERCHANT')")
    public ProductResponse getStock(Integer limit, Integer offset) {
        return productService.getPartnerProducts(limit, offset);
    }


    public PartnerStock update(PartnerStock stock) throws ProductNotFoundException {
        TenantStock tenantStock = stock.getId()!=null ? tenantStockRepository.findById(stock.getId()).orElse(null) : null;
        if(tenantStock != null){
            if(!productService.exists(stock.getProductRef()))
                throw new ProductNotFoundException("the product with the given ref doesn't exist");
            tenantStock = new TenantStock();
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

        TenantProduct tenantProduct = productService.getPartnerProductByRef(stock.getProductRef());
        tenantProduct.addStock(tenantStock);

        productService.save(tenantProduct);

        return tenantStockMapper.toDto(tenantStock);
    }
}

