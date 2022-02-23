package com.badals.shop.graph;

import com.badals.shop.service.dto.ProductDTO;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductResponse extends MutationResponse implements Serializable {
    List<ProductDTO> items;
    int total;
    boolean hasMore;
    LocalDateTime timestamp = LocalDateTime.now();
}
