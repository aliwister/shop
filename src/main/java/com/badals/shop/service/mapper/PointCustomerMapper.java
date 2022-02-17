package com.badals.shop.service.mapper;

import com.badals.shop.domain.PointCustomer;
import com.badals.shop.service.dto.PointCustomerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link PointCustomer} and its DTO {@link PointCustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {CustomerMapper.class})
public interface PointCustomerMapper extends EntityMapper<PointCustomerDTO, PointCustomer> {

    @Mapping(source = "customer.id", target = "customerId")
    PointCustomerDTO toDto(PointCustomer pointCustomer);

    @Mapping(source = "customerId", target = "customer")
    PointCustomer toEntity(PointCustomerDTO pointCustomerDTO);

    default PointCustomer fromId(Long id) {
        if (id == null) {
            return null;
        }
        PointCustomer pointCustomer = new PointCustomer();
        pointCustomer.setId(id);
        return pointCustomer;
    }
}
