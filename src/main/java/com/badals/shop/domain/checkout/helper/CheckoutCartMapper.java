package com.badals.shop.domain.checkout.helper;

import com.badals.shop.domain.Cart;
import com.badals.shop.domain.checkout.CheckoutCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {CheckoutLineItemMapper.class, CheckoutAddressMapper.class})
public interface CheckoutCartMapper {
   CheckoutCartMapper INSTANCE = Mappers.getMapper(CheckoutCartMapper.class);

   @Mapping(source = "cartItems", target = "items")
   @Mapping(target = "carrier", ignore = true)
   @Mapping(target = "currency", ignore = true)
   @Mapping(source = "customer.addresses", target = "addresses")
   CheckoutCart cartToCheckoutCart(Cart cart);
}
