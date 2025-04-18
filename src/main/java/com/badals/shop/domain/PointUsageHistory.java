package com.badals.shop.domain;

import com.badals.shop.aop.tenant.TenantSupport;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Data
@NoArgsConstructor
@Table(name = "point_usage_history", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class PointUsageHistory extends CheckoutAuditable<Long> implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "reward_id")
    private Long rewardId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "checkout_id")
    private Long checkoutId;

    @Column(name = "points")
    private Integer points;

    public PointUsageHistory( Long rewardId, Long customerId, String tenantId, Long checkoutId, Integer points) {
        this.rewardId = rewardId;
        this.customerId = customerId;
        this.tenantId = tenantId;
        this.checkoutId = checkoutId;
        this.points = points;
    }
}
