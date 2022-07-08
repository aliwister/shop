package com.badals.shop.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Address.
 */
@Entity
@Data
@Table(catalog="profileshop", name = "ps_address")
public class Address implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_address")
    private Long id;



    @Column(name = "id_country")
    private Long idCountry;

    @Column(name = "firstname")
    private String firstName;

    @Column(name = "lastname")
    private String lastName;

    @Column(name = "address1")
    private String line1;

    @Column(name = "address2")
    private String line2;

    @Column(name="postcode")
    private String postCode;

    @Column(name = "city")
    private String city;

    @Column(name = "phone")
    private String mobile;



    @Column(name = "alias")
    private String alias;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "deleted")
    private Boolean deleted;

    @Column(name = "plus_code")
    private String plusCode;

    private String lng;
    private String lat;




    private String state;
    private String country;




/*ALTER TABLE shop.address ADD lng varchar(100) NULL;
    ALTER TABLE shop.address ADD lat varchar(100) NULL;
    ALTER TABLE shop.address ADD plus_code varchar(100) NULL;
*/

    @ManyToOne
    @JoinColumn(name = "id_customer")
    @JsonIgnoreProperties("addresses")
    private Customer customer;




    public Address firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }


    public Address lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }



    public Address line1(String line1) {
        this.line1 = line1;
        return this;
    }

    public Address line2(String line2) {
        this.line1 = line1;
        return this;
    }

    public Address city(String city) {
        this.city = city;
        return this;
    }


    public Address mobile(String mobile) {
        this.mobile = mobile;
        return this;
    }


    public Address active(Boolean active) {
        this.active = active;
        return this;
    }


    public Address customer(Customer customer) {
        this.customer = customer;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        return id != null && id.equals(((Address) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Address{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +

            ", city='" + getCity() + "'" +
            ", mobile='" + getMobile() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
