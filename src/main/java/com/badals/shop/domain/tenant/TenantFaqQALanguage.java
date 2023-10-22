package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "faq_qa_language", catalog = "profileshop")
@Data
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantFaqQALanguage implements Serializable, TenantSupport {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question")
    private String question;

    @Column(name = "answer")
    private String answer;

    @Column(name = "language")
    private String language;

    @Column(name = "tenant_id")
    private String tenantId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "faq_qa_id", referencedColumnName = "id")
    private TenantFaqQA faqQA;

}
