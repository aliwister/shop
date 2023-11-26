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
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    @Column(name = "description", columnDefinition = "string")
    List<I18String> description = new ArrayList<>();

    @Type(type = "json")
    @Column(name = "rules", columnDefinition = "string")
    List<DiscountRule> rules = new ArrayList<>();

    @Type(type = "json")
    @Column(name = "checkout_rules", columnDefinition = "string")
    List<DiscountCheckoutRule> checkoutRules = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "filter", nullable = false)
    private DiscountType discountType = DiscountType.ALL;

    @Enumerated(EnumType.STRING)
    @Column(name = "reduction_type", nullable = false)
    private DiscountReductionType reductionType = DiscountReductionType.AMOUNT;

    @Type(type = "json")
    @Column(name = "discount")
    PriceMap discount;

    @Column(name = "customer_id", updatable = false, insertable = false)
    private Long customerId;

    @Column(name="tenant_id")
    private String tenantId;

}
