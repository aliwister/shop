package com.badals.shop.domain.tenant;

import com.badals.shop.domain.pojo.CarrierProfile;
import com.badals.shop.domain.pojo.PaymentProfile;
import lombok.Data;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.ParamDef;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * A Tenant.
 */
@Entity
@Data
@Table(catalog = "profileshop", name = "tenant")
@FilterDef(name = "tenantFilter", parameters = {@ParamDef(name = "tenantId", type = "string")})
@Filter(name = "tenantFilter", condition = "tenant_id = :tenantId")
public class Tenant implements Serializable {

   private static final long serialVersionUID = 1L;

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   @Column(name = "name")
   private String name;

   @Column(name = "customDomain")
   private String domain;

   @Column(name = "subdomain")
   private String subdomain;

   @Column(name = "plan")
   private String plan;

   @Column(name = "logo")
   private String logo;

   @Column(name = "mobile_logo")
   private String mobileLogo;

   @Column(name = "discount_rate")
   private Integer discountRate;

   @Column(name = "active")
   private Boolean active;

   @Column(name = "is_subdomain")
   private Boolean isSubdomain;

   @Column(name = "is_profile_auth")
   private Boolean profileAuth;

   @Column(name = "monthly_fee")
   private BigDecimal monthlyFee;

   @Column(name = "tenant_id")
   private String tenantId;

   @Column(name = "google_ads")
   private String googleAds;

   @Column(name = "plan_name")
   private String planName;

   @Column(name = "max_products")
   private Long maxProducts;

   @CreatedDate
   @Column(name = "created_date")
   private LocalDate createdDate;

   @Type(type = "json")
   @Column(name = "payment_profile", columnDefinition = "string")
   private PaymentProfile paymentProfile;

   @Type(type = "json")
   @Column(name = "carrier_profile", columnDefinition = "string")
   private CarrierProfile carrierProfile;


   @Override
   public boolean equals(Object o) {
      if (this == o) {
         return true;
      }
      if (!(o instanceof Tenant)) {
         return false;
      }
      return id != null && id.equals(((Tenant) o).id);
   }

   @Override
   public int hashCode() {
      return 31;
   }

   @Override
   public String toString() {
      return "Tenant{" +
              "id=" + getId() +
              ", name='" + getName() + "'" +
              "}";
   }
}
