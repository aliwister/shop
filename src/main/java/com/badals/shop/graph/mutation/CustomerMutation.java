package com.badals.shop.graph.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.graph.AddressResponse;
import com.badals.shop.service.AddressService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.AddressDTO;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.Locale;


/*
mutation {
  createNewProduct(product: {ref: 12, parent: 13, sku: "abc", upc: 334343, release_date: "2017-07-09"}) {
    ref,
    release_date
  }
}

 */

@Component
public class CustomerMutation implements GraphQLMutationResolver {

    private final AddressService addressService;
    private final UserService userService;

    public CustomerMutation(AddressService addressService, UserService userService) {
        this.addressService = addressService;
        this.userService = userService;
    }

    public String resetPassword(final String  password) {
        return "";
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public AddressResponse saveAddress(AddressDTO address) {
        if (address.getId() != null && addressService.isAddressUsed(address.getId()) ) {
            addressService.retireAddress(address.getId());
            address.setId(null);
        }

        Locale l = LocaleContextHolder.getLocale();
        //Long countryId = addressService.getCountryFromLocale(l.getCountry());

        address.setCountry(l.getCountry());
        address.setActive(true);
        address.setDeleted(false);
        Customer loginUser = userService.getUserWithAuthorities().orElse(new Customer(1L));

        address.setCustomerId(loginUser.getId());
        AddressDTO ret = addressService.save(address);
        return new AddressResponse(ret, "200", true, "Save Successful");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public AddressResponse deleteAddress(Long id) {
        // Ensure user is matching

        if (addressService.isAddressUsed(id) ) {
            addressService.deleteAddressByFlag(id);
        }
        else {
            addressService.delete(id);
        }
        return new AddressResponse(null, "200", true, "Delete Successful");
    }
}

