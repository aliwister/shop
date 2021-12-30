package com.badals.shop.service.mapper;

import com.badals.shop.domain.CartItem;
import com.badals.shop.domain.TenantCartItem;
import com.badals.shop.service.dto.CartItemDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * Mapper for the entity {@link CartItem} and its DTO {@link CartItemDTO}.
 */
@Mapper(componentModel = "spring", uses = {TenantCartMapper.class, TenantProfileProductMapper.class})
public interface TenantCartItemMapper extends EntityMapper<CartItemDTO, TenantCartItem> {

    @Mapping(source = "cart.id", target = "cartId")
    @Mapping(source = "product.ref", target = "productId")
    @Mapping(source = "product.ref", target = "id")
    @Mapping(source = "product.title", target = "title")
    @Mapping(source = "product.slug", target = "slug")
    @Mapping(source = "product.image", target = "image")
    @Mapping(source = "product.merchantId", target = "merchantId")
    @Mapping(ignore = true, target = "price")
    @Mapping(ignore = true, target = "salePrice")
    CartItemDTO toDto(TenantCartItem cartItem);

    @Mapping(source = "cartId", target = "cart")
    //@Mapping(source = "productId", target = "product.ref")
    @Mapping(source = "productId", target = "productId")
    TenantCartItem toEntity(CartItemDTO cartItemDTO);

    @AfterMapping
    default void afterMapping(@MappingTarget TenantCartItem target, CartItemDTO source) {
        if (target.getQuantity() == null) {
            target.setQuantity(1);
        }
    }

    @AfterMapping
    default void afterMapping(@MappingTarget CartItemDTO target, TenantCartItem source) {
        if (target.getImage() != null && !target.getImage().startsWith("https://"))
            if(target.getMerchantId() == 1)
                target.setImage("https://m.media-amazon.com/images/I/"+target.getImage());

        target.setPrice(source.getProduct().getPrice().getAmount().toString());
        target.setSalePrice(source.getProduct().getPrice().getAmount().toString());
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
