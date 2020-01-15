package com.badals.shop.vendor.amazon.pas.mappings;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EditorialReviewNode {
   String Source;
   String Content;
   
}
