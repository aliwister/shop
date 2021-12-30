package com.badals.shop.graph.query;

import com.badals.shop.domain.CheckoutCart;
import com.badals.shop.service.CartService;
import com.badals.shop.service.RewardService;

import com.badals.shop.service.dto.RewardDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.context.i18n.LocaleContextHolder;
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
    private final RewardService rewardService;

    public CartQuery(CartService cartService, RewardService rewardService) {
        this.cartService = cartService;
        this.rewardService = rewardService;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
/*
    public CartDTO getCart(final String secureKey, final List<CartItemDTO> items) {
        Locale l = LocaleContextHolder.getLocale();
        return this.cartService.updateCart(secureKey, items, false);
    }
*/

    public List<RewardDTO> rewards() {
        return rewardService.findAll();
    }


    public CheckoutCart plusCart(final String secureKey) {
        return cartService.plusCart(secureKey);
    }
}

