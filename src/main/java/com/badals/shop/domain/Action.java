package com.badals.shop.domain;

import com.badals.shop.domain.enumeration.OrderState;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "action")
public class Action extends Auditable implements Serializable {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

   private String action;
   private String object;
   @Column(name = "object_id")
   private String objectId;
   private String state;
   private String comment;

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
