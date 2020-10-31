package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CustomerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Named("mapWithoutAddresses")
    @Mapping(source = "pointCustomer.totalPoints", target = "totalPoints")
    @Mapping(source = "pointCustomer.spentPoints", target = "spentPoints")
    CustomerDTO toDtoWitAddresses(Customer customer);

    @Mapping(source = "pointCustomer.totalPoints", target = "totalPoints")
    @Mapping(source = "pointCustomer.spentPoints", target = "spentPoints")
    @Mapping(target = "addresses", ignore = true)
    CustomerDTO toDto(Customer customer);


    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
