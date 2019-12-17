package com.badals.shop.service;

import com.badals.shop.service.dto.OrderDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {
    public OrderDTO save(OrderDTO orderDTO) {
        return new OrderDTO();
    }
}
