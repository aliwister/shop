package com.badals.shop.service.query;

import com.badals.shop.service.CartService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import java.util.List;
import java.util.Locale;

@Component
public class CartQuery extends ShopQuery implements GraphQLQueryResolver {


    @Autowired
    private CartService cartService;

    @Deprecated
    public CartDTO updateCart(final CartDTO cart, final List<CartItemDTO> items) {
        //locale.
        Locale l = LocaleContextHolder.getLocale();
        return this.cartService.save(cart, items);
    }

}

