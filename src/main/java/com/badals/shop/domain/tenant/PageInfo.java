package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "page_info", catalog = "profileshop")
@Data
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class PageInfo implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "slug")
    private String slug;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "info")
    private String info;

    @Column(name = "language")
    private String language;

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

}
