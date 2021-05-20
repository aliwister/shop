package com.badals.shop.graph.query;

import lombok.Data;

import java.util.List;

@Data
public class AddressFormat {

   String inputFormat;
   String displayFormat;
   List<FieldDescription> descriptions;
   OptionType gmap;
}
