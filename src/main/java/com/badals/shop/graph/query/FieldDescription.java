package com.badals.shop.graph.query;

import lombok.Data;

import java.util.List;

@Data
public class FieldDescription {
   AddressField field;
   String label;
   Boolean required;
   String regex;
   FieldType fieldType;
   List<Option> options;
   final Integer minLength;
   final Integer maxLength;

   public FieldDescription(AddressField field, String label, Boolean required, String regex, FieldType fieldType, List<Option> options, Integer minLength, Integer maxLength) {
      this.field = field;
      this.label = label;
      this.required = required;
      this.regex = regex;
      this.fieldType = fieldType;
      this.options = options;
      this.minLength = minLength;
      this.maxLength = maxLength;
   }
}
