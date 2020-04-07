package com.badals.shop.service.query;

import com.badals.shop.domain.Customer;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.CustomerMapper;
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
public class CustomerQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private UserService userService;

    public List<AddressDTO> getAddresses(final int id) {
return null;
    }

    public List<CustomerDTO> customers() {
        return null;
    }

    public CustomerDTO me() {
        return  userService.getUserWithAuthorities().map(customerMapper::toDto).orElse(null);
    }
}

