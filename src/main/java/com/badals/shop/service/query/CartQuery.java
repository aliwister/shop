package com.badals.shop.service.query;

import com.badals.shop.service.CartService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CartQuery extends ShopQuery implements GraphQLQueryResolver {
    @Autowired
    private CartService cartService;

    public CartDTO updateCart(final CartDTO cart, final List<CartItemDTO> items) {
        return this.cartService.save(cart, items);
    }

}

