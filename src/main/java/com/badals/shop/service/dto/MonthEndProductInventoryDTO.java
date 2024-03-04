package com.badals.shop.service.dto;

import com.badals.shop.domain.pojo.PriceMap;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MonthEndProductInventoryDTO {
    private String title;
    private String sku;
    private PriceMap value;
    private BigDecimal quantity;
}
