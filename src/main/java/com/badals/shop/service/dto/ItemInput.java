package com.badals.shop.service.dto;

import lombok.Data;

@Data
public class ItemInput {
    private String name;
    private String description;
    private String harmonizedCode;
    private String countryOfManufacture;
    private Integer quantity;
    private String quantityUnits;
    private WeightInput weight;
    private CustomsValueInput customsValue;
    private String partNumber;
    private Integer numberOfPieces;
    private UnitPriceInput unitPrice;
}
