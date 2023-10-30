package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.pojo.TenantFaqQALanguage;
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

    @Type(type = "json")
    @Column(name = "qa", columnDefinition = "string")
    private List<TenantFaqQALanguage> faqQALanguages = new ArrayList<>();

    @Getter
    @Setter
    @Column(name = "faq_category_id", insertable = false, updatable = false)
    private Long categoryId;

    @ManyToOne()
    @JoinColumn(name = "faq_category_id", referencedColumnName = "id")
    private TenantFaqCategory tenantFaqCategory;


}
