package com.badals.shop.service.mutation;

import com.badals.shop.service.CartService;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.pojo.CheckoutSession;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/*
mutation {
  updateCart(
    cart: {id: 19, gift: true, giftMessage: "hello", cartState: UNCLAIMED}
    items: [{productId: 339, quantity: 2}, {productId: 338, quantity: 2}]
  ) {
    secureKey
    gift
    cartItems {
      quantity
      productId
    }
  }
}

mutation {
  createCheckoutSession(
    cart: {id: 19, secureKey:"fuck", gift: true, giftMessage: "hello", cartState: UNCLAIMED, customerId: 1}
    items: [{productId: 339, quantity: 2}, {productId: 338, quantity: 2}]
  ) {
    secureKey
    redirectUrl
  }
}


 */

@Component
public class CartMutation implements GraphQLMutationResolver {
    @Autowired
    private CartService cartService;

    public CartDTO updateCart(final String secureKey, final List<CartItemDTO> items) {
        return this.cartService.updateCart(secureKey, items, true);
    }

    public CartDTO setCart(final String secureKey, final List<CartItemDTO> items) {
        return this.cartService.updateCart(secureKey, items, false);
    }

    public CheckoutSession createCheckoutSession(final String secureKey, final List<CartItemDTO> items) {
        return cartService.createCheckout(secureKey, items);
    }
}

