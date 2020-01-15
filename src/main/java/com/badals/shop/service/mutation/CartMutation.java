package com.badals.shop.service.mutation;

import com.badals.shop.service.CartService;
import com.badals.shop.service.dto.CartDTO;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/*
mutation {
  createNewProduct(product: {ref: 12, parent: 13, sku: "abc", upc: 334343, release_date: "2017-07-09"}) {
    ref,
    release_date
  }
}

 */

@Component
public class CartMutation implements GraphQLMutationResolver {
    @Autowired
    private CartService cartService;

    public CartDTO updateCart(final CartDTO cart) {
        return this.cartService.save(cart);
    }

}

