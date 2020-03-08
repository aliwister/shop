package com.badals.shop.domain.pojo;

import com.badals.shop.service.dto.ProductDTO;
import lombok.Data;

import java.util.List;

@Data
public class ProductResponse {
    List<ProductDTO> items;
    int total;
    boolean hasMore;
}
