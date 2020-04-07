package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.service.dto.TenantDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "spring", uses = {MerchantMapper.class, CustomerMapper.class})
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {


    @Mapping(target = "removeMerchant", ignore = true)
    @Mapping(target = "removeCustomer", ignore = true)

    default Tenant fromId(Long id) {
        if (id == null) {
            return null;
        }
        Tenant tenant = new Tenant();
        tenant.setId(id);
        return tenant;
    }
}
