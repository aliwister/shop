package com.badals.shop.service.mutation;

import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.repository.CheckoutCartRepository;
import com.badals.shop.service.CartService;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.pojo.CheckoutSession;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;


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

    private final  CartService cartService;

    private final CheckoutCartRepository checkoutCartRepository;


    @Value("${badals.checkout-app}")
    String checkoutAppUrl;

    @Autowired
    private LocaleResolver locale;

    public CartMutation(CartService cartService, CheckoutCartRepository checkoutCartRepository) {
        this.cartService = cartService;
        this.checkoutCartRepository = checkoutCartRepository;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CartDTO updateCart(final String secureKey, final List<CartItemDTO> items) {
        Locale l = LocaleContextHolder.getLocale();
        return this.cartService.updateCart(secureKey, items, true);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CartDTO setCart(final String secureKey, final List<CartItemDTO> items) {
        return this.cartService.updateCart(secureKey, items, false);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CheckoutSession createCheckoutSession(final String secureKey, final List<CartItemDTO> items) {
        String token = cartService.createCheckout(secureKey, items);
        return new CheckoutSession(checkoutAppUrl + "?token=" + token, token);
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CheckoutCart createCheckoutSessionWithCart(final String secureKey, final List<CartItemDTO> items) {
        CheckoutCart cart = cartService.createCheckoutWithCart(secureKey, items);
        return cart;
    }

/*    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CheckoutCart createCart(CheckoutCart cart) {
        cart = cartService.createCustomCart(cart);
        //cart = checkoutCartRepository.save(cart);
        return cart;
    }*/

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CheckoutCart createCart(CheckoutCart cart) {
        cart.setSecureKey(CartService.createUIUD());
        cart = checkoutCartRepository.save(cart);
        return cart;
    }
}

