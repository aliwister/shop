package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.CartItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {CartMapper.class, ProductMapper.class})
public interface CartItemMapper extends EntityMapper<CartItemDTO, CartItem> {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.ref", target = "id")
    @Mapping(source = "product.title", target = "title")
    @Mapping(source = "product.slug", target = "slug")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.url", target = "url")
    @Mapping(source = "product.price.amount", target = "price", qualifiedByName = "doubleToString")
    @Mapping(source = "product.price.amount", target = "salePrice", qualifiedByName = "doubleToString")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(source = "cartId", target = "cart")
    @Mapping(source = "id", target = "product")
    //@Mapping(source = "customerId", target = "customer")
    CartItem toEntity(CartItemDTO cartItemDTO);

    default CartItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        CartItem cartItem = new CartItem();
        cartItem.setId(id);
        return cartItem;
    }
}
