package com.badals.shop.service.query;

import com.badals.shop.service.CartService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

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
public class OrderQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    CartService cartService;


    @Autowired
    private ProductService productService;

    public OrderDTO getOrder(int orderId) {
        return null;
    }



    //public String getCheckout()
}

