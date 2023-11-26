package com.badals.shop.domain;

import com.badals.shop.aop.tenant.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@Table(name = "point", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Point extends CheckoutAuditable<Long> implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "order_id")
    private Long orderId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "active")
    private Boolean active;

}
