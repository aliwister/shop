package com.badals.shop.graph;

import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.ProductDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class OrderResponse extends MutationResponse implements Serializable {
    List<OrderDTO> items;
    int total;
    boolean hasMore;
}
