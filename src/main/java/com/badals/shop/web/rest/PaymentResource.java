package com.badals.shop.web.rest;

import com.badals.shop.service.PaymentService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.PaymentDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.badals.shop.domain.Payment}.
 */
@RestController
@RequestMapping("/api")
public class PaymentResource {

    private final Logger log = LoggerFactory.getLogger(PaymentResource.class);

    private static final String ENTITY_NAME = "orderPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentService orderPaymentService;

    public PaymentResource(PaymentService orderPaymentService) {
        this.orderPaymentService = orderPaymentService;
    }

    /**
     * {@code POST  /order-payments} : Create a new orderPayment.
     *
     * @param orderPaymentDTO the orderPaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orderPaymentDTO, or with status {@code 400 (Bad Request)} if the orderPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/order-payments")
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO orderPaymentDTO) throws URISyntaxException {
        log.debug("REST request to save Payment : {}", orderPaymentDTO);
        if (orderPaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentDTO result = orderPaymentService.save(orderPaymentDTO);
        return ResponseEntity.created(new URI("/api/order-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /order-payments} : Updates an existing orderPayment.
     *
     * @param orderPaymentDTO the orderPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orderPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the orderPaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orderPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/order-payments")
    public ResponseEntity<PaymentDTO> updatePayment(@Valid @RequestBody PaymentDTO orderPaymentDTO) throws URISyntaxException {
        log.debug("REST request to update Payment : {}", orderPaymentDTO);
        if (orderPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentDTO result = orderPaymentService.save(orderPaymentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orderPaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /order-payments} : get all the orderPayments.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orderPayments in body.
     */
    @GetMapping("/order-payments")
    public List<PaymentDTO> getAllPayments() {
        log.debug("REST request to get all Payments");
        return orderPaymentService.findAll();
    }

    /**
     * {@code GET  /order-payments/:id} : get the "id" orderPayment.
     *
     * @param id the id of the orderPaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orderPaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/order-payments/{id}")
    public ResponseEntity<PaymentDTO> getPayment(@PathVariable Long id) {
        log.debug("REST request to get Payment : {}", id);
        Optional<PaymentDTO> orderPaymentDTO = orderPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderPaymentDTO);
    }

    /**
     * {@code DELETE  /order-payments/:id} : delete the "id" orderPayment.
     *
     * @param id the id of the orderPaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/order-payments/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        log.debug("REST request to delete Payment : {}", id);
        orderPaymentService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
