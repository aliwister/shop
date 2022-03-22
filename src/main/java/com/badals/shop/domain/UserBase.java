package com.badals.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class UserBase implements Serializable  {
   @CreatedBy
   @Size(max = 50)
   private String firstname;

   @CreatedDate
   @Size(max = 50)
   private String lastname;

   @NotNull
   @Column(nullable = false)
   private boolean active = false;

   //@Email
   @Size(min = 5, max = 254)
   @Column(length = 254, unique = true)
   private String email;

   @JsonIgnore
   @NotNull
   @Size(min = 60, max = 60)
   @Column(name = "passwd", length = 60, nullable = false)
   private String password;
}
