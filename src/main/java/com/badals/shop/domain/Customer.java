package com.badals.shop.domain;

import com.badals.shop.aop.tenant.TenantSupport;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Customer.
 */
@Entity
@Table(name = "ps_customer", catalog = "prestashop7")
@FilterDef(name = "TENANT_FILTER", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "TENANT_FILTER", condition = "tenant_id = :tenantId")
public class Customer extends UserBase implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    public Customer() {
    }

    public Customer(Long id) {
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_customer")
    private Long id;

    @Column(name = "company")
    private String company;

    @Column(name = "siret")
    private String siret;

    @Column(name = "ape")
    private String ape;

    @Column(name = "secure_key")
    private String secureKey;

    @Column(name = "oc_salt")
    private String salt;


    @Getter @Setter
    @Column(name = "allow_pickup")
    private Boolean allowPickup;

    @Getter @Setter
    @Column(name = "plus_discount")
    private Integer plusDiscount;

    @Getter @Setter
    @Column(name = "shipper_markup")
    private Integer shipperMarkup;

    @Column(name = "reset_password_token")
    private String resetKey;

    @Column(name = "reset_password_validity")
    private Instant resetDate;

    @Column
    private String mobile;

    @Column(name="tenant_id")
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @OneToOne(optional = true)
    @JoinColumn(name="id_customer", referencedColumnName = "customer_id")
    PointCustomer pointCustomer;

    public PointCustomer getPointCustomer() {
        return pointCustomer;
    }

    public void setPointCustomer(PointCustomer pointCustomer) {
        this.pointCustomer = pointCustomer;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResetKey() {
        return resetKey;
    }

    public void setResetKey(String resetKey) {
        this.resetKey = resetKey;
    }

    public Instant getResetDate() {
        return resetDate;
    }

    public void setResetDate(Instant resetDate) {
        this.resetDate = resetDate;
    }

    @OneToMany(mappedBy = "customer")
    @JsonIgnoreProperties("customer")
    private List<Address> addresses;

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @JsonIgnore
    @ManyToMany
    @JoinTable(
        name = "jhi_user_authority",
        joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id_customer")},
        inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")})

    @BatchSize(size = 20)
    private Set<Authority> authorities = new HashSet<>();


    public Set<Authority> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(Set<Authority> authorities) {
        this.authorities = authorities;
    }

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompany() {
        return company;
    }

    public Customer company(String company) {
        this.company = company;
        return this;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getSiret() {
        return siret;
    }

    public Customer siret(String siret) {
        this.siret = siret;
        return this;
    }

    public void setSiret(String siret) {
        this.siret = siret;
    }

    public String getApe() {
        return ape;
    }

    public Customer ape(String ape) {
        this.ape = ape;
        return this;
    }

    public void setApe(String ape) {
        this.ape = ape;
    }

    public Customer email(String email) {
        this.setEmail(email);
        return this;
    }


    public Customer password(String passwd) {
        this.setPassword(passwd);
        return this;
    }



    public String getSecureKey() {
        return secureKey;
    }

    public Customer secureKey(String secureKey) {
        this.secureKey = secureKey;
        return this;
    }

    public void setSecureKey(String secureKey) {
        this.secureKey = secureKey;
    }

    public String getSalt() {
        return salt;
    }

    public Customer salt(String salt) {
        this.salt = salt;
        return this;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }


    public Customer active(Boolean active) {
        this.setActive(active);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return id != null && id.equals(((Customer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", company='" + getCompany() + "'" +
            ", siret='" + getSiret() + "'" +
            ", ape='" + getApe() + "'" +
            ", firstname='" + getFirstname() + "'" +
            ", lastname='" + getLastname() + "'" +
            ", email='" + getEmail() + "'" +
            ", passwd='" + getPassword() + "'" +
            ", secureKey='" + getSecureKey() + "'" +
            ", salt='" + getSalt() + "'" +
            ", active=" + isActive() +
            "}";
    }

}
