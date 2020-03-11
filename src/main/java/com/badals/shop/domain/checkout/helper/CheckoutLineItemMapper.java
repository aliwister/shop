package com.badals.shop.domain.checkout.helper;

import com.badals.shop.domain.Cart;
import com.badals.shop.domain.CartItem;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.repository.projection.CartItemInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", uses = {})
public interface CheckoutLineItemMapper {
   //@Mapping(source = "cartItems", target = "price")
   @Mapping(source = "title", target = "name")
   LineItem cartItemToLineItem(CartItemInfo cartItem);
}
