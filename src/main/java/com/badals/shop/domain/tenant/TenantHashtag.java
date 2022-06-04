package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.Auditable;
import com.badals.shop.domain.pojo.I18String;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * A Hashtag.
 */
@Entity
@Data
@Table(name = "hashtag", catalog = "profileshop")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class TenantHashtag extends Auditable implements Serializable, TenantSupport {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Type(type = "json")
    @Column(name = "lang", columnDefinition = "string")
    private List<I18String> langs;

    private String icon;
    private String name;

    private Integer position;

/*    @ManyToOne
    @JoinColumn(name="tenant_id", referencedColumnName = "name", insertable = false, updatable = false)
    Tenant tenant;*/

    public TenantHashtag icon(String icon) {
        this.icon = icon;
        return this;
    }

    public TenantHashtag name(String name) {
        this.name = name;
        return this;
    }
    public TenantHashtag position(Integer position) {
        this.position = position;
        return this;
    }

    @Column(name="tenant_id")
    private String tenantId;

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TenantHashtag)) {
            return false;
        }
        return id != null && id.equals(((TenantHashtag) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Hashtag{" +
            "id=" + getId() +

            "}";
    }
}
