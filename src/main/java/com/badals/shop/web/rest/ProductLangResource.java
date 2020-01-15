package com.badals.shop.web.rest;

import com.badals.shop.service.ProductLangService;
import com.badals.shop.service.dto.ProductLangDTO;
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
 * REST controller for managing {@link com.badals.shop.domain.ProductLang}.
 */
@RestController
@RequestMapping("/api")
public class ProductLangResource {

    private final Logger log = LoggerFactory.getLogger(ProductLangResource.class);

    private static final String ENTITY_NAME = "productLang";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductLangService productLangService;

    public ProductLangResource(ProductLangService productLangService) {
        this.productLangService = productLangService;
    }

    /**
     * {@code POST  /product-langs} : Create a new productLang.
     *
     * @param productLangDTO the productLangDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new productLangDTO, or with status {@code 400 (Bad Request)} if the productLang has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/product-langs")
    public ResponseEntity<ProductLangDTO> createProductLang(@Valid @RequestBody ProductLangDTO productLangDTO) throws URISyntaxException {
        log.debug("REST request to save ProductLang : {}", productLangDTO);
        if (productLangDTO.getId() != null) {
            throw new BadRequestAlertException("A new productLang cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductLangDTO result = productLangService.save(productLangDTO);
        return ResponseEntity.created(new URI("/api/product-langs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /product-langs} : Updates an existing productLang.
     *
     * @param productLangDTO the productLangDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated productLangDTO,
     * or with status {@code 400 (Bad Request)} if the productLangDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productLangDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/product-langs")
    public ResponseEntity<ProductLangDTO> updateProductLang(@Valid @RequestBody ProductLangDTO productLangDTO) throws URISyntaxException {
        log.debug("REST request to update ProductLang : {}", productLangDTO);
        if (productLangDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductLangDTO result = productLangService.save(productLangDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, productLangDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /product-langs} : get all the productLangs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of productLangs in body.
     */
    @GetMapping("/product-langs")
    public List<ProductLangDTO> getAllProductLangs() {
        log.debug("REST request to get all ProductLangs");
        return productLangService.findAll();
    }

    /**
     * {@code GET  /product-langs/:id} : get the "id" productLang.
     *
     * @param id the id of the productLangDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the productLangDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/product-langs/{id}")
    public ResponseEntity<ProductLangDTO> getProductLang(@PathVariable Long id) {
        log.debug("REST request to get ProductLang : {}", id);
        Optional<ProductLangDTO> productLangDTO = productLangService.findOne(id);
        return ResponseUtil.wrapOrNotFound(productLangDTO);
    }

    /**
     * {@code DELETE  /product-langs/:id} : delete the "id" productLang.
     *
     * @param id the id of the productLangDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/product-langs/{id}")
    public ResponseEntity<Void> deleteProductLang(@PathVariable Long id) {
        log.debug("REST request to delete ProductLang : {}", id);
        productLangService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
