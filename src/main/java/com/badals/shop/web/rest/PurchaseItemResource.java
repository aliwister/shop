package com.badals.shop.web.rest;

import com.badals.shop.service.PurchaseItemService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.PurchaseItemDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.PurchaseItem}.
 */
@RestController
@RequestMapping("/api")
public class PurchaseItemResource {

    private final Logger log = LoggerFactory.getLogger(PurchaseItemResource.class);

    private static final String ENTITY_NAME = "purchaseItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PurchaseItemService purchaseItemService;

    public PurchaseItemResource(PurchaseItemService purchaseItemService) {
        this.purchaseItemService = purchaseItemService;
    }

    /**
     * {@code POST  /purchase-items} : Create a new purchaseItem.
     *
     * @param purchaseItemDTO the purchaseItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new purchaseItemDTO, or with status {@code 400 (Bad Request)} if the purchaseItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/purchase-items")
    public ResponseEntity<PurchaseItemDTO> createPurchaseItem(@Valid @RequestBody PurchaseItemDTO purchaseItemDTO) throws URISyntaxException {
        log.debug("REST request to save PurchaseItem : {}", purchaseItemDTO);
        if (purchaseItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new purchaseItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PurchaseItemDTO result = purchaseItemService.save(purchaseItemDTO);
        return ResponseEntity.created(new URI("/api/purchase-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /purchase-items} : Updates an existing purchaseItem.
     *
     * @param purchaseItemDTO the purchaseItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated purchaseItemDTO,
     * or with status {@code 400 (Bad Request)} if the purchaseItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the purchaseItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/purchase-items")
    public ResponseEntity<PurchaseItemDTO> updatePurchaseItem(@Valid @RequestBody PurchaseItemDTO purchaseItemDTO) throws URISyntaxException {
        log.debug("REST request to update PurchaseItem : {}", purchaseItemDTO);
        if (purchaseItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PurchaseItemDTO result = purchaseItemService.save(purchaseItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, purchaseItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /purchase-items} : get all the purchaseItems.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of purchaseItems in body.
     */
    @GetMapping("/purchase-items")
    public List<PurchaseItemDTO> getAllPurchaseItems(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all PurchaseItems");
        return purchaseItemService.findAll();
    }

    /**
     * {@code GET  /purchase-items/:id} : get the "id" purchaseItem.
     *
     * @param id the id of the purchaseItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the purchaseItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/purchase-items/{id}")
    public ResponseEntity<PurchaseItemDTO> getPurchaseItem(@PathVariable Long id) {
        log.debug("REST request to get PurchaseItem : {}", id);
        Optional<PurchaseItemDTO> purchaseItemDTO = purchaseItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(purchaseItemDTO);
    }

    /**
     * {@code DELETE  /purchase-items/:id} : delete the "id" purchaseItem.
     *
     * @param id the id of the purchaseItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/purchase-items/{id}")
    public ResponseEntity<Void> deletePurchaseItem(@PathVariable Long id) {
        log.debug("REST request to delete PurchaseItem : {}", id);
        purchaseItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
