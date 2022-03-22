package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

enum PaymentType implements Serializable {
   BANK("bankwire", "Bank Muscat Transfer", "bank-transfer.svg", false),
   STRIPE("stripe", "Stripe", "bank-transfer.svg", true),

   CHECKOUT("checkoutcom", "Credit/Debit Card", "credit-card.svg", true);
   public final String ref;
   public final String label;
   public final String image;
   public final boolean prePay;

   PaymentType(String ref, String label, String image, boolean prePay) {
      this.ref = ref;
      this.label = label;
      this.image = image;
      this.prePay = prePay;
   }
}

@Data
public class PaymentProfile implements Serializable {
   List<PaymentDef> payments = new ArrayList<>();

   public PaymentDef getType(PaymentType type) {
      return payments.stream().filter(i -> i.name.equals(type.ref)).findFirst().orElse(null);
   }

   public void addPayment(PaymentDef def) {
      payments.add(def);
   }


/*
   private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
   {
      payments = (ArrayList) aInputStream.readObject();
   }

   private void writeObject(ObjectOutputStream aOutputStream) throws IOException
   {
      aOutputStream.writeObject(payments);
   }*/
}

