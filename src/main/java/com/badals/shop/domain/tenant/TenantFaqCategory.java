package com.badals.shop.domain.tenant;


import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.pojo.TenantFaqCategoryName;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "faq_category", catalog = "profileshop")
@Data
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantFaqCategory implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position")
    private int position;

    @Column(name = "tenant_id")
    private String tenantId;

    @Column(name = "enabled")
    private Boolean enabled;

    @Type(type = "json")
    @Column(name = "name", columnDefinition = "string")
    private List<TenantFaqCategoryName> faqCategoryNames = new ArrayList<>();

    @OneToMany(mappedBy = "tenantFaqCategory", cascade=CascadeType.ALL, orphanRemoval = true)
    private List<TenantFaqQA> faqQAs = new ArrayList<>();

}
