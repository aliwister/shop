package com.badals.shop.graph;

import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.OrderDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class CartResponse extends MutationResponse implements Serializable {
    CartDTO cart;
    int total;
    boolean hasMore;
}
