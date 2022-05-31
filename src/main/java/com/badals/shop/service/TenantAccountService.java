package com.badals.shop.service;

import com.badals.shop.domain.Order;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.tenant.TenantCart;
import com.badals.shop.domain.tenant.TenantOrder;
import com.badals.shop.graph.OrderResponse;
import com.badals.shop.repository.AddressRepository;
import com.badals.shop.repository.TenantOrderRepository;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.mapper.CheckoutAddressMapper;
import com.badals.shop.service.mapper.TenantOrderMapper;
import com.badals.shop.service.util.MailService;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import org.hibernate.envers.AuditReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class TenantAccountService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final TenantOrderRepository orderRepository;
    //private final OrderSearchRepository orderSearchRepository;

    private final TenantOrderMapper orderMapper;
    private UserService userService;

    private final CustomerService customerService;
    private final MessageSource messageSource;
    private final MailService mailService;
    private final AuditReader auditReader;
    private final CheckoutAddressMapper checkoutAddressMapper;
    private final AddressRepository addressRepository;


    public TenantAccountService(TenantOrderRepository orderRepository, TenantOrderMapper orderMapper, UserService userService, CustomerService customerService, MessageSource messageSource, MailService mailService, AuditReader auditReader, CheckoutAddressMapper checkoutAddressMapper, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.customerService = customerService;
        this.messageSource = messageSource;
        this.mailService = mailService;
        this.auditReader = auditReader;
        this.checkoutAddressMapper = checkoutAddressMapper;
        this.addressRepository = addressRepository;
    }

    public OrderResponse orders(List<OrderState> orderState, Integer limit, Integer offset) {
        Page<TenantOrder> orders = orderRepository.findAllByOrderStateInOrderByCreatedDateDesc(orderState, PageRequest.of((int) offset/limit,limit));
        OrderResponse response = new OrderResponse();
        response.setTotal(orders.getSize());
        response.setItems(orders.stream().map(orderMapper::toDto).collect(Collectors.toList()));
        Integer total = orderRepository.countForState(orderState);
        response.setHasMore((limit+offset) < total);
        return response;
    }

    public OrderDTO findOrderByRef(String ref) throws OrderNotFoundException {
        return orderRepository.findForOrderDetails(0L, ref).map(orderMapper::toDto).orElseThrow(() -> new OrderNotFoundException("Invalid Order"));

    }

}