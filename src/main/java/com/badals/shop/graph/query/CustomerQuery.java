package com.badals.shop.graph.query;

import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.CustomerService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.AddressDTO;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.mapper.CustomerMapper;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import lombok.Data;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private final ProductService productService;

    private final CustomerMapper customerMapper;

    private final UserService userService;
    private final CustomerService customerService;

    public CustomerQuery(ProductService productService, CustomerMapper customerMapper, UserService userService, CustomerService customerService) {
        this.productService = productService;
        this.customerMapper = customerMapper;
        this.userService = userService;
        this.customerService = customerService;
    }

    public List<AddressDTO> getAddresses(final int id) {
return null;
    }

    public List<CustomerDTO> customers() {
        return null;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomerDTO me() {
        return  userService.getUserWithAuthorities().map(customerMapper::toDto).orElse(null);
    }

    @PreAuthorize("hasRole('ROLE_NONE')")
    public CustomerDTO meTest(Long id) {
        return userService.getUserWithAuthorities(id).map(customerMapper::toDto).orElse(null);
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public CustomerDTO customer(String mobile) {
        return customerService.findByMobileJoinAddresses(mobile);
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public AddressFormat addressDescription(String isoCode, String lang) {
            AddressFormat addressFormat = new AddressFormat();
            addressFormat.inputFormat = "{alias}_{firstName}{lastName}_{line1}_{line2}_{city}_{country}{state}{postalCode}_{mobile}";
            addressFormat.displayFormat =  "({alias}){firstName}{lastName}_{line1}_{line2}_{state}_{mobile}";
            addressFormat.gmap = OptionType.REQUIRED;
            addressFormat.descriptions = new ArrayList<>() {{
                add(new FieldDescription(AddressField.firstName, "First Name", true, "/^[a-zA-Z]{3,10}$/", FieldType.TEXT, null));
                add(new FieldDescription(AddressField.lastName, "Last Name", true, "/^[a-zA-Z]{3,10}$/", FieldType.TEXT, null));
                add(new FieldDescription(AddressField.line1, "Address", true, "/^.{3,50}$/", FieldType.TEXT, null));
                add(new FieldDescription(AddressField.line2, "Landmark", true, "/^.{3,50}$/", FieldType.TEXT, null));
                add(new FieldDescription(AddressField.postalCode, "Postal Code", true, "/^[0-9]{3}$/", FieldType.TEXT, null));
                add(new FieldDescription(AddressField.state, "State", true, "", FieldType.SELECT,
                        new ArrayList<>(){{
                            add(new Option("Muscat", "MA"));
                            add(new Option("Al-Dakhilia", "DA"));
                            add(new Option("Al-Batinah North", "BS"));
                            add(new Option("Al-Batinah South", "BJ"));
                            add(new Option("Al-Wusta", "WU"));
                            add(new Option("Al-Sharqiya North", "SS"));
                            add(new Option("Al-Sharqiya South", "SJ"));
                            add(new Option("Al-Dhahira", "ZA"));
                            add(new Option("Al-Buraymi", "BU"));
                            add(new Option("Musandam", "MU"));
                            add(new Option("Dhofar", "ZU"));
                        }}));
                add(new FieldDescription(AddressField.country, "Country", true, "", FieldType.SELECT,
                        new ArrayList<>(){{
                            add(new Option("Oman", "OM"));
                        }}));
                add(new FieldDescription(AddressField.city, "City", true, "/(.*[a-z]){3}/i", FieldType.REMOTE_SELECT, null));
                add(new FieldDescription(AddressField.mobile, "Mobile", true, "/(.*[a-z]){3}/i", FieldType.TEXT, null));
                add(new FieldDescription(AddressField.alias, "Alias", true, "/(.*[a-z]){3}/i", FieldType.SELECT, new ArrayList<>(){{
                    add(new Option("Home", "home"));
                    add(new Option("Work", "work"));
                    add(new Option("Other", "other"));
                }}));
            }};

            return addressFormat;

    }

}

