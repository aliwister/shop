package com.badals.shop.domain;

import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.pojo.ProductI18;
import com.badals.shop.domain.pojo.VariationOption;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class AlgoliaProduct implements Serializable {

    //private Long id;

    @JsonProperty("objectID")
    private Long ref;
    private Long upc;
    private String sku;
    private Map<String, String> title = new HashMap<>();
    private String slug;

    private Map<String, Map<String, String>> hierarchicalCategories = new HashMap<>();
    private Map<String, String> brand = new HashMap<>();
    //private Map<String, String> availability = new HashMap<>();

    private Map<String, BigDecimal> price = new HashMap<>();
    private String image;
    private Long quantity;

    private String tenant;
    private Map<String, String> merchant = new HashMap<>();

    private String unit;

    private Integer discountInPercent;

    private Integer availability;
    private BigDecimal rating;

    private Boolean isPrime;
    private String shipsFrom;

    private Boolean isVariation;

    private Map<String, String> attributes = new HashMap<>();
    private Map<String, List<VariationOption>> variationOptions = new HashMap<>();

    private BigDecimal weight;
}
