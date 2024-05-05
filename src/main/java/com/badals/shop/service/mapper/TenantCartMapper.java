package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantCartItem;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Currency;
import java.util.Locale;


@Mapper(componentModel = "spring", uses = {TenantCartItemMapper.class})
public interface TenantCartMapper extends EntityMapper<CartDTO, TenantCart> {

    @Mapping(target = "cartRule", source = "cart.cartRule")
    @Mapping(target = "adjustments", source = "cart.adjustments")
    @Mapping(target = "additionalInfo", source = "cart.additionalInfo")
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

    @AfterMapping
    default void afterMapping(@MappingTarget CartDTO target, TenantCart source) {
        Locale locale = LocaleContextHolder.getLocale();
        target.setCurrency(Currency.getInstance(locale).getCurrencyCode());
    }
}

