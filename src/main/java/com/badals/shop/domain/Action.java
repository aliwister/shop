package com.badals.shop.domain;

import com.badals.shop.domain.enumeration.OrderState;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "action")
@EntityListeners(AuditingEntityListener.class)
public class Action implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String action;
   private String object;
   @Column(name = "object_id")
   private String objectId;
   private String state;
   private String comment;

   @CreatedDate
   @Column(name = "created_date")
   private Date createdDate;

   public Date getCreatedDate() {
      return createdDate;
   }

   public void setCreatedDate(Date createdDate) {
      this.createdDate = createdDate;
   }

   public String getCreatedBy() {
      return createdBy;
   }

   public void setCreatedBy(String createdBy) {
      this.createdBy = createdBy;
   }

   @Column(name = "created_by")
   private String createdBy;


   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getAction() {
      return action;
   }

   public void setAction(String action) {
      this.action = action;
   }

   public String getObject() {
      return object;
   }

   public void setObject(String object) {
      this.object = object;
   }

   public String getObjectId() {
      return objectId;
   }

   public void setObjectId(String objectId) {
      this.objectId = objectId;
   }

   public String getState() {
      return state;
   }

   public void setState(String state) {
      this.state = state;
   }

   public String getComment() {
      return comment;
   }

   public void setComment(String comment) {
      this.comment = comment;
   }
}
