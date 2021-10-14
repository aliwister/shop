package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;

@Data
public class I18String implements Serializable {
   public I18String() {
   }

   public I18String(String lang, String value) {
      this.lang = lang;
      this.value = value;
   }

   String lang;
   String value;
}
