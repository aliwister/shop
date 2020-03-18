package com.badals.shop.web.rest;

import com.badals.shop.service.ProductOverrideService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.ProductOverrideDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.ProductOverride}.
 */
@RestController
@RequestMapping("/api")
public class ProductOverrideResource {

    private final Logger log = LoggerFactory.getLogger(ProductOverrideResource.class);

    private static final String ENTITY_NAME = "productOverride";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOverrideService productOverrideService;

    public ProductOverrideResource(ProductOverrideService productOverrideService) {
        this.productOverrideService = productOverrideService;
    }

    /**
     * {@code POST  /product-overrides} : Create a new productOverride.
     *
     * @param productOverrideDTO the productOverrideDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productOverrideDTO, or with status {@code 400 (Bad Request)} if the productOverride has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-overrides")
    public ResponseEntity<ProductOverrideDTO> createProductOverride(@Valid @RequestBody ProductOverrideDTO productOverrideDTO) throws URISyntaxException {
        log.debug("REST request to save ProductOverride : {}", productOverrideDTO);
        if (productOverrideDTO.getId() != null) {
            throw new BadRequestAlertException("A new productOverride cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOverrideDTO result = productOverrideService.save(productOverrideDTO);
        return ResponseEntity.created(new URI("/api/product-overrides/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-overrides} : Updates an existing productOverride.
     *
     * @param productOverrideDTO the productOverrideDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productOverrideDTO,
     * or with status {@code 400 (Bad Request)} if the productOverrideDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOverrideDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-overrides")
    public ResponseEntity<ProductOverrideDTO> updateProductOverride(@Valid @RequestBody ProductOverrideDTO productOverrideDTO) throws URISyntaxException {
        log.debug("REST request to update ProductOverride : {}", productOverrideDTO);
        if (productOverrideDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductOverrideDTO result = productOverrideService.save(productOverrideDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productOverrideDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-overrides} : get all the productOverrides.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productOverrides in body.
     */
    @GetMapping("/product-overrides")
    public List<ProductOverrideDTO> getAllProductOverrides() {
        log.debug("REST request to get all ProductOverrides");
        return productOverrideService.findAll();
    }

    /**
     * {@code GET  /product-overrides/:id} : get the "id" productOverride.
     *
     * @param id the id of the productOverrideDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productOverrideDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-overrides/{id}")
    public ResponseEntity<ProductOverrideDTO> getProductOverride(@PathVariable Long id) {
        log.debug("REST request to get ProductOverride : {}", id);
        Optional<ProductOverrideDTO> productOverrideDTO = productOverrideService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productOverrideDTO);
    }

    /**
     * {@code DELETE  /product-overrides/:id} : delete the "id" productOverride.
     *
     * @param id the id of the productOverrideDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-overrides/{id}")
    public ResponseEntity<Void> deleteProductOverride(@PathVariable Long id) {
        log.debug("REST request to delete ProductOverride : {}", id);
        productOverrideService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
