package com.badals.shop.graph;

import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaymentResponse extends MutationResponse implements Serializable {
    List<PaymentDTO> items;
    int total;
    boolean hasMore;
}
