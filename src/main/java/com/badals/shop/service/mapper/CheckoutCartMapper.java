package com.badals.shop.service.mapper;

import com.badals.shop.domain.Cart;
import com.badals.shop.domain.CheckoutCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CheckoutLineItemMapper.class, CheckoutAddressMapper.class})
public interface CheckoutCartMapper {
   CheckoutCartMapper INSTANCE = Mappers.getMapper(CheckoutCartMapper.class);

   @Mapping(source = "cartItems", target = "items")
   @Mapping(target = "carrier", ignore = true)
   @Mapping(source = "customer.addresses", target = "addresses")
   @Mapping(source = "customer.firstname", target = "name")
   @Mapping(source = "customer.email", target = "email")
   @Mapping(source = "currency.code", target = "currency")
   CheckoutCart cartToCheckoutCart(Cart cart);
}
