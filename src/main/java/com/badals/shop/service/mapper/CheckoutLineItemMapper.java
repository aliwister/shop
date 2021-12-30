package com.badals.shop.service.mapper;

import com.badals.shop.domain.pojo.LineItem;
import com.badals.shop.repository.projection.CartItemInfo;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = {})
public interface CheckoutLineItemMapper {
   //@Mapping(source = "cartItems", target = "price")
   @Mapping(source = "title", target = "name")
   @Mapping(target="unit", ignore = true)
   LineItem cartItemToLineItem(CartItemInfo cartItem);

   @AfterMapping
   default void afterMapping(@MappingTarget LineItem target, CartItemInfo source) {
      if (target.getImage() != null && !target.getImage().startsWith("https://"))
         if (source.getMerchantId() != null && source.getMerchantId() == 1)
            target.setImage("https://m.media-amazon.com/images/I/" + target.getImage());
   }
}
