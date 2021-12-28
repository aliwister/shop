package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.service.dto.CartDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {ProfileCartItemMapper.class})
public interface ProfileCartMapper extends EntityMapper<CartDTO, TenantCart> {

    CartDTO toDto(TenantCart cart);

    TenantCart toEntity(CartDTO cartDTO);

    default TenantCart fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantCart cart = new TenantCart();
        cart.setId(id);
        return cart;
    }
}

