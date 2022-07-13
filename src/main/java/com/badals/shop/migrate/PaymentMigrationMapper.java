package com.badals.shop.migrate;

import com.badals.shop.domain.Payment;
import com.badals.shop.domain.tenant.TenantPayment;
import com.badals.shop.service.mapper.EntityMapper;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/**
 * Mapper for the entity {@link Payment} and its DTO
 */
@Mapper(componentModel = "spring", uses = {OrderMigrationMapper.class})
public interface PaymentMigrationMapper {

    PaymentMigrationMapper INSTANCE = Mappers.getMapper( PaymentMigrationMapper.class );

    @Mapping(target = "order", ignore = true)
    //@Mapping(target = "id", ignore = true)
    TenantPayment toDto(Payment payment);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantPayment target, Payment source) {
        target.setCurrency("OMR");
    }
}
