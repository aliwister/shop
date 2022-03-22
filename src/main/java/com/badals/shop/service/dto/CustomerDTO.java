package com.badals.shop.service.dto;
import com.badals.shop.domain.Customer;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the {@link Customer} entity.
 */
@Data
public class CustomerDTO implements Serializable {

    private Long id;

    private String company;

    private String siret;

    private String ape;

    @NotNull
    private String firstname;

    @NotNull
    private String lastname;

    @NotNull
    private String email;

    @NotNull
    private String passwd;

    private String secureKey;

    private String salt;

    private Boolean active;
    private Boolean allowPickup;
    private Integer plusDiscount;
    private Integer shipperMarkup;

    private String mobile;

    private Long totalPoints;
    private Long spentPoints;

    private List<AddressDTO> addresses;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerDTO customerDTO = (CustomerDTO) o;
        if (customerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerDTO{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", siret='" + getSiret() + "'" +
            ", ape='" + getApe() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", passwd='" + getPasswd() + "'" +
            ", secureKey='" + getSecureKey() + "'" +
            ", salt='" + getSalt() + "'" +
            ", active=" + getActive() +
            "}";
    }
}
