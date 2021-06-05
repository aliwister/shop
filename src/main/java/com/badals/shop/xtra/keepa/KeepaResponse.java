package com.badals.shop.xtra.keepa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KeepaResponse {
   List<KProduct> products;
   String processingTimeInMs;
}
