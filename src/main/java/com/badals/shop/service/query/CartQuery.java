package com.badals.shop.service.query;

import com.badals.shop.service.CartService;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.dto.CustomerDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;

/*
query {
  product(id: 1) {
    id,
    ref,
    parent,
    sku,
    image,
    images
  }
}
query {
  product(id: 1) {
    ref
    releaseDate
    variationOptions {
      name
      values
    }
    variationAttributes {
      name
      value
    }
    variationDimensions
  }
}

 */

@Component
public class CartQuery extends ShopQuery implements GraphQLQueryResolver {

    private final CartService cartService;

    public CartQuery(CartService cartService) {
        this.cartService = cartService;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CartDTO getCart(final String secureKey, final List<CartItemDTO> items) {
        Locale l = LocaleContextHolder.getLocale();
        return this.cartService.updateCart(secureKey, items, false);
    }
}

