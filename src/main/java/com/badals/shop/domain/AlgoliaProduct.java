package com.badals.shop.domain;

import com.badals.shop.domain.enumeration.Condition;
import com.badals.shop.domain.pojo.ProductI18;
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
    //private Long parent;
    private Long upc;
    private String sku;
    private String title;
    private String title_ar;
    private String slug;

    Map hierarchicalCategories;
    Map hierarchicalCategories_ar;


    private String brand;
    private String brand_ar;

    private String availability;
    private String availability_ar;

    //private String group;

    private Map<String, BigDecimal> price;
    private String image;
    private Long quantity;
    private String tenant;
    private String merchant;
    private String unit;

    private Integer discountInPercent;

    private Long hours;

    //private LocalDate releaseDate;

    @NotNull
    //private Boolean active;

    @Lob
    //private List<String> similarProducts;

    //private String url;

    //private Condition condition;
    //private Boolean isUsed;
    //private Boolean availableForOrder;

    private BigDecimal weight;
    //private BigDecimal volumeWeight;

}
