package com.badals.shop.service.mapper;

import com.badals.shop.domain.tenant.TenantCartItem;
import com.badals.shop.service.dto.CartItemDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Currency;
import java.util.Locale;

import static com.badals.shop.service.CurrencyService.BASE_CURRENCY_KEY;

/**
 * Mapper for the entity {@link TenantCartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {TenantCartMapper.class, TenantProductMapper.class})
public interface TenantCartItemMapper extends EntityMapper<CartItemDTO, TenantCartItem> {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.ref", target = "productId")
    @Mapping(source = "product.ref", target = "id")
    @Mapping(source = "product.title", target = "title")
    @Mapping(source = "product.slug", target = "slug")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.merchantId", target = "merchantId")
    @Mapping(source = "product.listPrice", target = "listPrice", qualifiedByName = "withCurrencyConversionMap")
    @Mapping(source = "product.price", target = "price", qualifiedByName = "withCurrencyConversionMap")
    CartItemDTO toDto(TenantCartItem cartItem);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantCartItem target, CartItemDTO source) {
        if (target.getQuantity() == null) {
            target.setQuantity(1);
        }
    }

    @AfterMapping
    default void afterMapping(@MappingTarget CartItemDTO target, TenantCartItem source) {
        Locale locale = LocaleContextHolder.getLocale();
        String targetCurrency = Currency.getInstance(locale).getCurrencyCode();
        target.setCurrency(targetCurrency);
    }

/*    @AfterMapping
    default void afterMapping(@MappingTarget CartItemDTO target, CartItem source) {
        if (source.getProduct() != null && source.getProduct().getVariationAttributes() != null)
            target.setVariationAttributes(source.getProduct().getVariationAttributes().stream().map(v -> v.getValue()).collect(Collectors.toList()));
    }*/


    default TenantCartItem fromId(Long id) {
        if (id == null) {
            return null;
        }
        TenantCartItem cartItem = new TenantCartItem();
        cartItem.setId(id);
        return cartItem;
    }
}
