package com.badals.shop.service.dto;
import com.badals.shop.domain.pojo.AddressPojo;
import com.badals.shop.domain.Address;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link Address} entity.
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

    private Boolean active;
    private Boolean deleted;

    private String alias;

    private Long customerId;

    private String postalCode;
    private String plusCode;
    private String lat;
    private String lng;
    private String country;
    private String state;

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
    public AddressDTO(String alias, String firstName, String lastName, String line1, String line2, String city, String state, String country, String postalCode, String mobile, String plusCode, String lng, String lat) {
        this.alias = alias;
        this.firstName = firstName;
        this.lastName = lastName;
        this.line1 = line1;
        this.line2 = line2;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.mobile = mobile;
        this.plusCode = plusCode;
        this.lng = lng;
        this.lat = lat;
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
        return new AddressDTO(pojo.getAlias(),pojo.getFirstName(),pojo.getLastName(),pojo.getLine1(),pojo.getLine2(),pojo.getCity(), pojo.getState(), pojo.getCountry(), pojo.getPostalCode(), pojo.getMobile(), pojo.getPlusCode(), pojo.getLng(), pojo.getLat());
    }
}
