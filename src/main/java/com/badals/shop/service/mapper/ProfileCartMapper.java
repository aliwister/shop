package com.badals.shop.service.mapper;

import com.badals.shop.domain.ProfileCart;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.mapper.EntityMapper;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {ProfileCartItemMapper.class})
public interface ProfileCartMapper extends EntityMapper<CartDTO, ProfileCart> {

    CartDTO toDto(ProfileCart cart);

    ProfileCart toEntity(CartDTO cartDTO);

    default ProfileCart fromId(Long id) {
        if (id == null) {
            return null;
        }
        ProfileCart cart = new ProfileCart();
        cart.setId(id);
        return cart;
    }
}

