package com.badals.shop.service.mapper;

import com.badals.shop.domain.Address;

import com.badals.shop.domain.pojo.AddressPojo;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CheckoutAddressMapper {

   AddressPojo addressToAddressPojo(Address address);

   Address addressPojoToAddress(AddressPojo addressPojo);
}
