package com.badals.shop.domain.tenant;

import com.badals.shop.aop.tenant.TenantSupport;
import com.badals.shop.domain.tenant.Tenant;
import com.badals.shop.domain.pojo.Price;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A MerchantStock.
 */
@Entity
@Table(name = "jhi_tenant_authority", catalog = "profileshop")
@SelectBeforeUpdate(false)
@Data
public class TenantAuthority implements Serializable {
   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "user_id")
   private Long userId;

   @Column(name = "tenant_id")
   private String tenantId;

   @ManyToOne
   @JoinColumn(name="tenant_id", referencedColumnName = "name", insertable = false, updatable = false)
   Tenant tenant;

   @Column(name = "authority_name")
   private String authority;

   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof TenantAuthority)) {
         return false;
      }
      return id != null && id.equals(((TenantAuthority) o).id);
   }

   @Override
   public int hashCode() {
      return 31;
   }

   @Override
   public String toString() {
      return "MerchantStock{" +
              "id=" + getId() +
              "}";
   }
}
