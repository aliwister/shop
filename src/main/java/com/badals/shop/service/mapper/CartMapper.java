package com.badals.shop.service.mapper;

import com.badals.shop.domain.*;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.service.dto.CartDTO;

import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.dto.ProductDTO;
import org.mapstruct.*;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Mapper for the entity {@link Cart} and its DTO {@link CartDTO}.
 */
@Mapper(componentModel = "spring", uses = {AddressMapper.class, CustomerMapper.class, CurrencyMapper.class, CarrierMapper.class, CartItemMapper.class})
public interface CartMapper extends EntityMapper<CartDTO, Cart> {

    @Mapping(source = "deliveryAddress.id", target = "deliveryAddressId")
    @Mapping(source = "invoiceAddress.id", target = "invoiceAddressId")
    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "carrier.id", target = "carrierId")
    CartDTO toDto(Cart cart);

    @Mapping(source = "deliveryAddressId", target = "deliveryAddress")
    @Mapping(source = "invoiceAddressId", target = "invoiceAddress")
    @Mapping(source = "customerId", target = "customer")
    @Mapping(source = "carrierId", target = "carrier")
    @Mapping(target = "cartItems", ignore = true)
    @Mapping(target = "removeCartItem", ignore = true)
    Cart toEntity(CartDTO cartDTO);

    default Cart fromId(Long id) {
        if (id == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(id);
        return cart;
    }

    @AfterMapping
    default void afterMapping(@MappingTarget CartDTO target, Cart source) {
        List<CartItemDTO> items = target.getCartItems();
        items = items.stream().filter(x -> x.getProductId() != null).collect(Collectors.toList());
        target.setCartItems(items);
    }

}
