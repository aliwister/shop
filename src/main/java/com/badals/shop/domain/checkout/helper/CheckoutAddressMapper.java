package com.badals.shop.domain.checkout.helper;

import com.badals.shop.domain.Address;
import com.badals.shop.domain.Cart;
import com.badals.shop.domain.checkout.CheckoutCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CheckoutAddressMapper {

   AddressPojo addressToAddressPojo(Address address);

   Address addressPojoToAddress(AddressPojo addressPojo);
}
