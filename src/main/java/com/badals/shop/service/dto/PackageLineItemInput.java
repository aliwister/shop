package com.badals.shop.service.dto;

import lombok.Data;

@Data
public class PackageLineItemInput {
    private WeightInput weight;
    private DimensionsInput dimensions;
}
