package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.PricingRequestDTO;

import com.badals.shop.service.dto.ProductLangDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PricingRequest} and its DTO {@link PricingRequestDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PricingRequestMapper extends EntityMapper<PricingRequestDTO, PricingRequest> {

    @Mapping(source = "product.sku", target = "ref")
    @Mapping(source = "product.parent.sku", target = "parent")
    @Mapping(source = "createdBy", target="email")
    PricingRequestDTO toDto(PricingRequest productLang);


    default PricingRequest fromId(Long id) {
        if (id == null) {
            return null;
        }
        PricingRequest pricingRequest = new PricingRequest();
        pricingRequest.setId(id);
        return pricingRequest;
    }
}
