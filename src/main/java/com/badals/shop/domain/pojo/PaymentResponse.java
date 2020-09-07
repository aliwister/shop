package com.badals.shop.domain.pojo;

import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PaymentDTO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PaymentResponse implements Serializable {
    List<PaymentDTO> items;
    int total;
    boolean hasMore;
}
