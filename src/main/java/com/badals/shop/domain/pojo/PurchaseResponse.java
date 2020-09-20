package com.badals.shop.domain.pojo;

import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PurchaseResponse implements Serializable {
    List<PurchaseDTO> items;
    int total;
    boolean hasMore;
}
