package com.badals.shop.service.dto;
import com.badals.shop.domain.checkout.helper.AddressPojo;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.badals.shop.domain.Address} entity.
 */
@Data
public class AddressDTO implements Serializable {

    private Long id;

    private String firstName;

    private String lastName;

    private String line1;

    private String line2;

    private String city;

    private String mobile;

    private String active;

    private String alias;

    private Long customerId;

    private String plusCode;
    private String lat;
    private String lng;
    private String country;

    public AddressDTO() {
    }

    public AddressDTO(String firstName, String lastName, String line1, String line2, String city) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        //this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "AddressDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", line1='" + line1 + '\'' +
                ", line2='" + line2 + '\'' +
                ", city='" + city + '\'' +
                ", mobile='" + mobile + '\'' +
                ", active='" + active + '\'' +
                ", customerId=" + customerId +
                '}';
    }

    public static AddressDTO fromAddressPojo(AddressPojo pojo) {
        if(pojo == null)
            return null;
        return new AddressDTO(pojo.getFirstName(),pojo.getLastName(),pojo.getLine1(),pojo.getLine2(),pojo.getCity());
    }
}
