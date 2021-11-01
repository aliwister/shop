package com.badals.shop.graph.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.checkout.helper.LineItem;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.graph.CartResponse;
import com.badals.shop.graph.CheckoutSessionResponse;
import com.badals.shop.repository.CheckoutCartRepository;
import com.badals.shop.repository.CustomerRepository;
import com.badals.shop.service.CartService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.pojo.CheckoutSession;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

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

    private final UserService userService;
    private final CustomerRepository customerRepository;
    @Value("${badals.checkout-app}")
    String checkoutAppUrl;


    public CartMutation(CartService cartService, CheckoutCartRepository checkoutCartRepository, UserService userService, CustomerRepository customerRepository) {
        this.cartService = cartService;
        this.checkoutCartRepository = checkoutCartRepository;
        this.userService = userService;
        this.customerRepository = customerRepository;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CartResponse updateCart(final String secureKey, final List<CartItemDTO> items) {
        Locale l = LocaleContextHolder.getLocale();
        CartDTO cart = this.cartService.updateCart(secureKey, items, true);
        CartResponse response = new CartResponse();
        response.setCart(cart);
        response.setSuccess(true);
        response.setMessage("Cart Saved Successfully");
        return response;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    public CheckoutSessionResponse createCheckout(final String secureKey) {
        String token = cartService.createCheckout(secureKey, null);

        CheckoutSessionResponse response = new CheckoutSessionResponse();
        response.setSecureKey(token);
        response.setMessage("Checkout Session created successfully");
        response.setSuccess(true);
        response.setCode("200");

        return response;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    public CheckoutCart createPlusCart(String secureKey, List<LineItem> items) {
        CheckoutCart cart = cartService.createCheckoutPlus(secureKey, items);
        return cart;
    }
}

