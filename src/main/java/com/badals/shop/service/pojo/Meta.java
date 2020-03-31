package com.badals.shop.service.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Data
public class Meta implements Serializable {
   String publisher = "test";
   String isbn = "test";
   String edition = "test";
   String country = "test";
   List<String> languages = Arrays.asList(new String []{"arabic","english"});
   String numberOfReader = "test";
   String numberOfPage = "test";
   String samplePDF = "test";
}
