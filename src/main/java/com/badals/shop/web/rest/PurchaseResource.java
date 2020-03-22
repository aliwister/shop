package com.badals.shop.web.rest;

import com.badals.shop.service.PurchaseService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.PurchaseDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.Purchase}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseResource.class);

    private static final String ENTITY_NAME = "purchase";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseService purchaseService;

    public PurchaseResource(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    /**
     * {@code POST  /purchases} : Create a new purchase.
     *
     * @param purchaseDTO the purchaseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseDTO, or with status {@code 400 (Bad Request)} if the purchase has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchases")
    public ResponseEntity<PurchaseDTO> createPurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) throws URISyntaxException {
        log.debug("REST request to save Purchase : {}", purchaseDTO);
        if (purchaseDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchase cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseDTO result = purchaseService.save(purchaseDTO);
        return ResponseEntity.created(new URI("/api/purchases/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchases} : Updates an existing purchase.
     *
     * @param purchaseDTO the purchaseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchases")
    public ResponseEntity<PurchaseDTO> updatePurchase(@Valid @RequestBody PurchaseDTO purchaseDTO) throws URISyntaxException {
        log.debug("REST request to update Purchase : {}", purchaseDTO);
        if (purchaseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseDTO result = purchaseService.save(purchaseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchases} : get all the purchases.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchases in body.
     */
    @GetMapping("/purchases")
    public List<PurchaseDTO> getAllPurchases() {
        log.debug("REST request to get all Purchases");
        return purchaseService.findAll();
    }

    /**
     * {@code GET  /purchases/:id} : get the "id" purchase.
     *
     * @param id the id of the purchaseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchases/{id}")
    public ResponseEntity<PurchaseDTO> getPurchase(@PathVariable Long id) {
        log.debug("REST request to get Purchase : {}", id);
        Optional<PurchaseDTO> purchaseDTO = purchaseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseDTO);
    }

    /**
     * {@code DELETE  /purchases/:id} : delete the "id" purchase.
     *
     * @param id the id of the purchaseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchases/{id}")
    public ResponseEntity<Void> deletePurchase(@PathVariable Long id) {
        log.debug("REST request to delete Purchase : {}", id);
        purchaseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
