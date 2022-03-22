package com.badals.shop.domain;

import com.badals.shop.config.Constants;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A user.
 */
@Data
@Entity
@Table(name = "ps_customer", catalog = "prestashop7")
public class User extends UserBase implements Serializable {

    private static final long serialVersionUID = 1L;

    private static String tenantFilter = "profileshop";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Long id;


    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "User{" +
            ", firstName='" + getFirstname() + '\'' +
            ", lastName='" + getLastname() + '\'' +
            ", email='" + getEmail() + '\'' +
            ", activated='" + isActive() + '\'' +
            "}";
    }
}
