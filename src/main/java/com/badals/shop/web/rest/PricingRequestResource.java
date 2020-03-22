package com.badals.shop.web.rest;

import com.badals.shop.service.PricingRequestService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.PricingRequestDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.PricingRequest}.
 */
@RestController
@RequestMapping("/api")
public class PricingRequestResource {

    private final Logger log = LoggerFactory.getLogger(PricingRequestResource.class);

    private static final String ENTITY_NAME = "pricingRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PricingRequestService pricingRequestService;

    public PricingRequestResource(PricingRequestService pricingRequestService) {
        this.pricingRequestService = pricingRequestService;
    }

    /**
     * {@code POST  /pricing-requests} : Create a new pricingRequest.
     *
     * @param pricingRequestDTO the pricingRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pricingRequestDTO, or with status {@code 400 (Bad Request)} if the pricingRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pricing-requests")
    public ResponseEntity<PricingRequestDTO> createPricingRequest(@Valid @RequestBody PricingRequestDTO pricingRequestDTO) throws URISyntaxException {
        log.debug("REST request to save PricingRequest : {}", pricingRequestDTO);
        if (pricingRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new pricingRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PricingRequestDTO result = pricingRequestService.save(pricingRequestDTO);
        return ResponseEntity.created(new URI("/api/pricing-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pricing-requests} : Updates an existing pricingRequest.
     *
     * @param pricingRequestDTO the pricingRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pricingRequestDTO,
     * or with status {@code 400 (Bad Request)} if the pricingRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pricingRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pricing-requests")
    public ResponseEntity<PricingRequestDTO> updatePricingRequest(@Valid @RequestBody PricingRequestDTO pricingRequestDTO) throws URISyntaxException {
        log.debug("REST request to update PricingRequest : {}", pricingRequestDTO);
        if (pricingRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PricingRequestDTO result = pricingRequestService.save(pricingRequestDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pricingRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /pricing-requests} : get all the pricingRequests.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pricingRequests in body.
     */
    @GetMapping("/pricing-requests")
    public List<PricingRequestDTO> getAllPricingRequests() {
        log.debug("REST request to get all PricingRequests");
        return pricingRequestService.findAll();
    }

    /**
     * {@code GET  /pricing-requests/:id} : get the "id" pricingRequest.
     *
     * @param id the id of the pricingRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pricingRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pricing-requests/{id}")
    public ResponseEntity<PricingRequestDTO> getPricingRequest(@PathVariable Long id) {
        log.debug("REST request to get PricingRequest : {}", id);
        Optional<PricingRequestDTO> pricingRequestDTO = pricingRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pricingRequestDTO);
    }

    /**
     * {@code DELETE  /pricing-requests/:id} : delete the "id" pricingRequest.
     *
     * @param id the id of the pricingRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pricing-requests/{id}")
    public ResponseEntity<Void> deletePricingRequest(@PathVariable Long id) {
        log.debug("REST request to delete PricingRequest : {}", id);
        pricingRequestService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
