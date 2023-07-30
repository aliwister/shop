package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Customer;
import com.badals.shop.domain.enumeration.DiscountReductionType;
import com.badals.shop.domain.enumeration.DiscountType;
import com.badals.shop.domain.pojo.DiscountCheckoutRule;
import com.badals.shop.domain.pojo.DiscountRule;
import com.badals.shop.domain.pojo.I18String;
import com.badals.shop.domain.pojo.PriceMap;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Cart.
 */
@Entity
@Data
@Table(name = "cart_rule", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantCartRule implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String coupon;

    @Column(name = "auto_apply")
    private Boolean autoApply;

    @Column(name = "can_combine")
    private Boolean canCombine;

    @Column(name = "enabled")
    private Boolean enabled;

    private Integer priority;

    @Type(type = "json")
    @Getter @Setter @Column(name = "description", columnDefinition = "string")
    List<I18String> description = new ArrayList<>();



    @Type(type = "json")
    @Getter @Setter @Column(name = "rules", columnDefinition = "string")
    List<DiscountRule> rules = new ArrayList<>();

    @Type(type = "json")
    @Getter @Setter @Column(name = "checkout_rules", columnDefinition = "string")
    List<DiscountCheckoutRule> checkoutRules = new ArrayList<>();

    //@NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "filter", nullable = false)
    private DiscountType discountType = DiscountType.ALL;

    @Enumerated(EnumType.STRING)
    @Column(name = "reduction_type", nullable = false)
    private DiscountReductionType reductionType = DiscountReductionType.AMOUNT;

    @Type(type = "json")
    @Column(name = "discount")
    PriceMap discount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties("carts")
    private Customer customer;


    @Getter
    @Setter
    @Column(name = "customer_id", updatable = false, insertable = false)
    private Long customerId;


    @OneToMany(mappedBy = "cart", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TenantCartItem> cartItems = new ArrayList<TenantCartItem>();

    @Column(name="tenant_id")
    private String tenantId;

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantCartRule)) {
            return false;
        }
        return id != null && id.equals(((TenantCartRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Cart{" +
            "id=" + getId() +

            "}";
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
