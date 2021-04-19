package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CustomerDTO;
import org.mapstruct.*;

import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Named("mapWithAddresses")
    @Mapping(source = "pointCustomer.totalPoints", target = "totalPoints")
    @Mapping(source = "pointCustomer.spentPoints", target = "spentPoints")
    CustomerDTO toDtoWitAddresses(Customer customer);

    @Named("mapWithoutAddresses")
    @Mapping(source = "pointCustomer.totalPoints", target = "totalPoints")
    @Mapping(source = "pointCustomer.spentPoints", target = "spentPoints")
    @Mapping(target = "addresses", ignore = true)
    CustomerDTO toDto(Customer customer);

    @BeanMapping(qualifiedByName = "plusCode")
    CustomerDTO toDtoWithMappedAddresses(Customer customer);

    @Named("plusCode")
    @AfterMapping
    default void doAfterMapping(@MappingTarget CustomerDTO dto) {
        dto.setAddresses(dto.getAddresses().stream().filter(x->x.getPlusCode() != null).collect(Collectors.toList()));
    }


    default Customer fromId(Long id) {
        if (id == null) {
            return null;
        }
        Customer customer = new Customer();
        customer.setId(id);
        return customer;
    }
}
