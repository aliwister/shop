package com.badals.shop.domain.tenant;


import com.badals.shop.aop.tenant.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

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

    @OneToMany(mappedBy = "faqCategory",cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TenantFaqCategoryName> faqCategoryNames = new ArrayList<>();

//    @OneToMany(mappedBy = "faqCategory",cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
//    private List<TenantFaqQA> faqQAs = new ArrayList<>();


}
