package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;

import com.badals.shop.service.dto.CartItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {CartMapper.class, ProductMapper.class, CustomerMapper.class})
public interface CartItemMapper extends EntityMapper<CartItemDTO, CartItem> {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "customer.id", target = "customerId")
    CartItemDTO toDto(CartItem cartItem);

    @Mapping(source = "cartId", target = "cart")
    @Mapping(source = "productId", target = "product")
    @Mapping(source = "customerId", target = "customer")
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
