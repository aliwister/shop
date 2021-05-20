package com.badals.shop.graph.query;

import lombok.Data;

import java.util.List;

@Data
public class FieldDescription {
   AddressField field;
   String label;
   Boolean isRequired;
   String regex;
   FieldType fieldType;
   List<Option> options;

   public FieldDescription(AddressField field, String label, Boolean isRequired, String regex, FieldType fieldType, List<Option> options) {
      this.field = field;
      this.label = label;
      this.isRequired = isRequired;
      this.regex = regex;
      this.fieldType = fieldType;
      this.options = options;
   }
}
