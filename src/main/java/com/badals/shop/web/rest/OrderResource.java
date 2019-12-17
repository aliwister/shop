package com.badals.shop.web.rest;

import com.badals.shop.service.OrderService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * OrderResource controller
 */
@RestController
@RequestMapping("/api/order")
public class OrderResource {

    private final Logger log = LoggerFactory.getLogger(OrderResource.class);
    private static final String ENTITY_NAME = "order";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrderService orderService;

    public OrderResource(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
    * GET defaultAction
    */
    @GetMapping("/default-action")
    public String defaultAction() {
        return "defaultAction";
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderDTO> createProduct(@RequestBody OrderDTO orderDTO) throws URISyntaxException {
        log.debug("REST request to save Order : {}", orderDTO);
        if (OrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderDTO result = orderService.save(orderDTO);
        return ResponseEntity.created(new URI("/api/orders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

}
