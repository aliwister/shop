package com.badals.shop.graph.query;

import com.badals.shop.domain.Customer;
import com.badals.shop.graph.AddressList;
import com.badals.shop.service.AddressService;
import com.badals.shop.service.CustomerService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.CustomerDTO;
import com.badals.shop.service.mapper.CustomerMapper;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    private final ProductService productService;
    private final AddressService addressService;

    private final CustomerMapper customerMapper;

    private final UserService userService;
    private final CustomerService customerService;

    public CustomerQuery(ProductService productService, AddressService addressService, CustomerMapper customerMapper, UserService userService, CustomerService customerService) {
        this.productService = productService;
        this.addressService = addressService;
        this.customerMapper = customerMapper;
        this.userService = userService;
        this.customerService = customerService;
    }

    public List<CustomerDTO> customers() {
        return null;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomerDTO me() {
       CustomerDTO customerDTO = userService.getUserWithAuthorities().map(customerMapper::toDto).orElse(null);
       return customerDTO;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public CustomerDTO mePlus() {
       Customer customer = userService.getUserWithAuthorities().orElse(null);
       if(customer != null)
          return customerService.findOne(customer.getId()).orElse(null);
       return null;
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
            addressFormat.inputFormat = "{alias}_{firstName}{lastName}_{line1}_{line2}_{city}{state}{postalCode}_{country}{mobile}_{save}";
            addressFormat.displayFormat =  "({alias}){firstName}{lastName}_{line1}_{line2}_{city}{state}_{mobile}";
            addressFormat.gmap = OptionType.REQUIRED;
            addressFormat.descriptions = new ArrayList<>() {{
                add(new FieldDescription(AddressField.firstName, "First Name", true, "/^[a-zA-Z]{3,10}$/", FieldType.TEXT, null, 2, 15));
                add(new FieldDescription(AddressField.lastName, "Last Name", true, "/^[a-zA-Z]{3,10}$/", FieldType.TEXT, null, 2, 15));
                add(new FieldDescription(AddressField.line1, "Address", true, "/^.{3,50}$/", FieldType.TEXT, null, 5, 30));
                add(new FieldDescription(AddressField.line2, "Landmark", true, "/^.{3,50}$/", FieldType.TEXT, null, 5, 30));
                add(new FieldDescription(AddressField.postalCode, "Postal Code", false, "/^[0-9]{3}$/", FieldType.TEXT, null, 3, 5));
                add(new FieldDescription(AddressField.state, "State", true, "", FieldType.SELECT,
                        new ArrayList<>(){{
                            add(new Option("Muscat", "MA"));
                            add(new Option("Ad Dakhiliyah", "DA"));
                            add(new Option("Al Batinah North", "BS"));
                            add(new Option("Al Batinah South", "BJ"));
                            add(new Option("Al Wusta", "WU"));
                            add(new Option("Ash Sharqiyah North", "SS"));
                            add(new Option("Ash Sharqiyah South", "SJ"));
                            add(new Option("Ad Dhahirah", "ZA"));
                            add(new Option("Al Buraymi", "BU"));
                            add(new Option("Musandam", "MU"));
                            add(new Option("Dhofar", "ZU"));
                        }}, null, null));
                add(new FieldDescription(AddressField.country, "Country", true, "", FieldType.SELECT,
                        new ArrayList<>(){{
                            add(new Option("Oman", "OM"));
                        }}, null, null));
                add(new FieldDescription(AddressField.city, "City", true, "/(.*[a-z]){3}/i", FieldType.TEXT, null, 2, 15));
                add(new FieldDescription(AddressField.mobile, "Mobile", true, "/(.*[a-z]){3}/i", FieldType.MOBILE, null, 11, 11));
                add(new FieldDescription(AddressField.alias, "Alias", true, "/(.*[a-z]){3}/i", FieldType.SELECT, new ArrayList<>(){{
                    add(new Option("Home", "home"));
                    add(new Option("Work", "work"));
                    add(new Option("Other", "other"));
                }}, null, null));
               add(new FieldDescription(AddressField.save, "Save this information for next time", false, null, FieldType.CHECKBOX, null, null, null));
            }};

            return addressFormat;
    }

    public AddressList addresses () {
       Customer loginUser = userService.getUserWithAuthorities().orElse(new Customer(1L));
       return new AddressList(addressService.customerAddresses(loginUser), "({alias}){firstName}{lastName}_{line1}_{line2}_{state}_{mobile}");
    }

}