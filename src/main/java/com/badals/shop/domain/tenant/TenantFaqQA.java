package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "faq_qa", catalog = "profileshop")
@Data
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantFaqQA implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position")
    private int position;

    @Column(name = "tenant_id")
    private String tenantId;

    @OneToMany(mappedBy = "faqQA",cascade=CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<TenantFaqQALanguage> faqQALanguages = new ArrayList<>();

    @Getter
    @Setter
    @Column(name = "faq_category_id", updatable = false, insertable = false)
    private Long categoryId;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "faq_category_id", referencedColumnName = "id")
    private TenantFaqCategory faqCategory;
}
