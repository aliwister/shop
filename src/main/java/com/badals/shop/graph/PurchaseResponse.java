package com.badals.shop.graph;

import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PurchaseResponse extends MutationResponse implements Serializable {
    List<PurchaseDTO> items;
    int total;
    boolean hasMore;
}
