package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "faq_category_name", catalog = "profileshop")
@Data
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantFaqCategoryName implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "language")
    private String language;

    @Column(name = "name")
    private String name;

    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "faq_category_id", referencedColumnName = "id")
    private TenantFaqCategory faqCategory;
}

