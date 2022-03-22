package com.badals.shop.web.rest;

import com.badals.shop.service.MerchantStockService;
import com.badals.shop.service.dto.StockDTO;
import com.badals.shop.web.rest.errors.BadRequestAlertException;

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
 * REST controller for managing {@link com.badals.shop.domain.MerchantStock}.
 */
@RestController
@RequestMapping("/api")
public class MerchantStockResource {

    private final Logger log = LoggerFactory.getLogger(MerchantStockResource.class);

    private static final String ENTITY_NAME = "merchantStock";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MerchantStockService merchantStockService;

    public MerchantStockResource(MerchantStockService merchantStockService) {
        this.merchantStockService = merchantStockService;
    }

    /**
     * {@code POST  /merchant-stocks} : Create a new merchantStock.
     *
     * @param stockDTO the merchantStockDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new merchantStockDTO, or with status {@code 400 (Bad Request)} if the merchantStock has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/merchant-stocks")
    public ResponseEntity<StockDTO> createMerchantStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to save MerchantStock : {}", stockDTO);
        if (stockDTO.getId() != null) {
            throw new BadRequestAlertException("A new merchantStock cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StockDTO result = merchantStockService.save(stockDTO);
        return ResponseEntity.created(new URI("/api/merchant-stocks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /merchant-stocks} : Updates an existing merchantStock.
     *
     * @param stockDTO the merchantStockDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated merchantStockDTO,
     * or with status {@code 400 (Bad Request)} if the merchantStockDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the merchantStockDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/merchant-stocks")
    public ResponseEntity<StockDTO> updateMerchantStock(@Valid @RequestBody StockDTO stockDTO) throws URISyntaxException {
        log.debug("REST request to update MerchantStock : {}", stockDTO);
        if (stockDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        StockDTO result = merchantStockService.save(stockDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stockDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /merchant-stocks} : get all the merchantStocks.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of merchantStocks in body.
     */
    @GetMapping("/merchant-stocks")
    public List<StockDTO> getAllMerchantStocks() {
        log.debug("REST request to get all MerchantStocks");
        return merchantStockService.findAll();
    }

    /**
     * {@code GET  /merchant-stocks/:id} : get the "id" merchantStock.
     *
     * @param id the id of the merchantStockDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the merchantStockDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/merchant-stocks/{id}")
    public ResponseEntity<StockDTO> getMerchantStock(@PathVariable Long id) {
        log.debug("REST request to get MerchantStock : {}", id);
        Optional<StockDTO> merchantStockDTO = merchantStockService.findOne(id);
        return ResponseUtil.wrapOrNotFound(merchantStockDTO);
    }

    /**
     * {@code DELETE  /merchant-stocks/:id} : delete the "id" merchantStock.
     *
     * @param id the id of the merchantStockDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/merchant-stocks/{id}")
    public ResponseEntity<Void> deleteMerchantStock(@PathVariable Long id) {
        log.debug("REST request to delete MerchantStock : {}", id);
        merchantStockService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
