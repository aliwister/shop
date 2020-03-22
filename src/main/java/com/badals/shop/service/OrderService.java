package com.badals.shop.service;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.repository.OrderRepository;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.mapper.OrderMapper;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Order}.
 */
@Service
@Transactional
public class OrderService {

    private final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    private final OrderMapper orderMapper;
    private UserService userService;

    private final CustomerService customerService;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper, UserService userService, CustomerService customerService) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.userService = userService;
        this.customerService = customerService;
    }

    /**
     * Save a order.
     *
     * @param orderDTO the entity to save.
     * @return the persisted entity.
     */
    public OrderDTO save(OrderDTO orderDTO) {
        log.debug("Request to save Order : {}", orderDTO);
        Order order = orderMapper.toEntity(orderDTO);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    /**
     * Get all the orders.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<OrderDTO> findAll() {
        log.debug("Request to get all Orders");
        return orderRepository.findAll().stream()
            .map(orderMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one order by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrderDTO> findOne(Long id) {
        log.debug("Request to get Order : {}", id);
        return orderRepository.findById(id)
            .map(orderMapper::toDto);
    }

    /**
     * Delete the order by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Order : {}", id);
        orderRepository.deleteById(id);
    }

    @Transactional
    public OrderDTO getOrderConfirmation(String reference, String confirmationKey) throws OrderNotFoundException {
        Order order = orderRepository.findOrderByReferenceAndConfirmationKey(reference, confirmationKey).orElse(null);
        if(order == null) {
            throw new OrderNotFoundException("Order Not Found");
        }
        Customer customer = customerService.findByEmail(order.getEmail());
        order.setCustomer(customer);
        order.setConfirmationKey(null);
        order = orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public List<OrderDTO> getCustomerOrders() {
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        return orderRepository.findOrdersByCustomerOrderByCreatedDateDesc(loginUser).stream()
                .map(orderMapper::toDto)
                .collect(Collectors.toCollection(LinkedList::new));
    }
}
