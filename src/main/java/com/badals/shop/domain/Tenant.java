package com.badals.shop.domain;

import javax.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A Tenant.
 */
@Entity
@Table(name = "tenant")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "max_products")
    private Long maxProducts;

    @Column(name = "plan_name")
    private String planName;

    @Column(name = "discount_rate")
    private Integer discountRate;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "monthly_fee")
    private BigDecimal monthlyFee;

    @Column(name = "sku_prefix")
    private String skuPrefix;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;


    public Long getMaxProducts() {
        return maxProducts;
    }

    public void setMaxProducts(Long maxProducts) {
        this.maxProducts = maxProducts;
    }

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public Integer getDiscountRate() {
        return discountRate;
    }

    public void setDiscountRate(Integer discountRate) {
        this.discountRate = discountRate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public BigDecimal getMonthlyFee() {
        return monthlyFee;
    }

    public void setMonthlyFee(BigDecimal monthlyFee) {
        this.monthlyFee = monthlyFee;
    }

    public String getSkuPrefix() {
        return skuPrefix;
    }

    public void setSkuPrefix(String skuPrefix) {
        this.skuPrefix = skuPrefix;
    }

    public LocalDate getContractStartDate() {
        return contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    @ManyToMany
    @JoinTable(name = "tenant_merchant",
               joinColumns = @JoinColumn(name = "tenant_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "merchant_id", referencedColumnName = "id"))
    private List<Merchant> merchants = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "tenant_customer",
               joinColumns = @JoinColumn(name = "tenant_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "id_customer"))
    private Set<Customer> customers = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Tenant name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Merchant> getMerchants() {
        return merchants;
    }

    public Tenant merchants(List<Merchant> merchants) {
        this.merchants = merchants;
        return this;
    }

    public Tenant addMerchant(Merchant merchant) {
        this.merchants.add(merchant);
        //merchant.getTenants().add(this);
        return this;
    }

    public Tenant removeMerchant(Merchant merchant) {
        this.merchants.remove(merchant);
        //merchant.getTenants().remove(this);
        return this;
    }

    public void setMerchants(List<Merchant> merchants) {
        this.merchants = merchants;
    }

    public Set<Customer> getCustomers() {
        return customers;
    }

    public Tenant customers(Set<Customer> customers) {
        this.customers = customers;
        return this;
    }

    public Tenant addCustomer(Customer customer) {
        this.customers.add(customer);
        //customer.getTenants().add(this);
        return this;
    }

    public Tenant removeCustomer(Customer customer) {
        this.customers.remove(customer);
        //customer.getTenants().remove(this);
        return this;
    }

    public void setCustomers(Set<Customer> customers) {
        this.customers = customers;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tenant)) {
            return false;
        }
        return id != null && id.equals(((Tenant) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Tenant{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
