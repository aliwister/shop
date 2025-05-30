package com.badals.shop.domain;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class Auditable<T> {
   @CreatedBy
   @Column(name = "created_by")
   private String createdBy;

   @CreatedDate
   @Column(name = "created_date")
   private Date createdDate;

   @LastModifiedBy
   @Column(name = "last_modified_by")
   private String lastModifiedBy;

   @LastModifiedDate
   @Column(name = "last_modified_date")
   private Date lastModifiedDate;
}
