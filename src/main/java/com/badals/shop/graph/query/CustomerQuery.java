package com.badals.shop.graph.query;

import com.badals.shop.domain.Customer;
import com.badals.shop.graph.AddressList;
import com.badals.shop.service.AddressService;
import com.badals.shop.service.CustomerService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.mapper.CustomerMapper;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
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
public class CustomerQuery extends BaseQuery implements GraphQLQueryResolver {

    private final AddressService addressService;

    private final CustomerMapper customerMapper;

    private final UserService userService;
    private final CustomerService customerService;

    public CustomerQuery(AddressService addressService, CustomerMapper customerMapper, UserService userService, CustomerService customerService) {
        this.addressService = addressService;
        this.customerMapper = customerMapper;
        this.userService = userService;
        this.customerService = customerService;
    }

    public List<CustomerDTO> customers() {
        return null;
    }



    @PreAuthorize("hasRole('ROLE_NONE')")
    public CustomerDTO meTest(Long id) {
        return customerService.getUserWithAuthorities(id).map(customerMapper::toDto).orElse(null);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public CustomerDTO customer(String mobile) {
        return customerService.findByMobileJoinAddresses(mobile);
    }

    //@PreAuthorize("hasRole('ROLE_USER')")


    public AddressList addresses () {
       Customer loginUser = customerService.getUserWithAuthorities().orElse(new Customer(1L));
       return new AddressList(addressService.customerAddresses(loginUser), "({alias}){firstName}{lastName}_{line1}_{line2}_{state}_{mobile}");
    }

}