package com.badals.shop.web.rest;

import com.badals.shop.service.PointCustomerService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.PointCustomerDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.PointCustomer}.
 */
@RestController
@RequestMapping("/api")
public class PointCustomerResource {

    private final Logger log = LoggerFactory.getLogger(PointCustomerResource.class);

    private static final String ENTITY_NAME = "pointCustomer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PointCustomerService pointCustomerService;

    public PointCustomerResource(PointCustomerService pointCustomerService) {
        this.pointCustomerService = pointCustomerService;
    }

    /**
     * {@code POST  /point-customers} : Create a new pointCustomer.
     *
     * @param pointCustomerDTO the pointCustomerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pointCustomerDTO, or with status {@code 400 (Bad Request)} if the pointCustomer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/point-customers")
    public ResponseEntity<PointCustomerDTO> createPointCustomer(@Valid @RequestBody PointCustomerDTO pointCustomerDTO) throws URISyntaxException {
        log.debug("REST request to save PointCustomer : {}", pointCustomerDTO);
        if (pointCustomerDTO.getId() != null) {
            throw new BadRequestAlertException("A new pointCustomer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PointCustomerDTO result = pointCustomerService.save(pointCustomerDTO);
        return ResponseEntity.created(new URI("/api/point-customers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /point-customers} : Updates an existing pointCustomer.
     *
     * @param pointCustomerDTO the pointCustomerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pointCustomerDTO,
     * or with status {@code 400 (Bad Request)} if the pointCustomerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pointCustomerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/point-customers")
    public ResponseEntity<PointCustomerDTO> updatePointCustomer(@Valid @RequestBody PointCustomerDTO pointCustomerDTO) throws URISyntaxException {
        log.debug("REST request to update PointCustomer : {}", pointCustomerDTO);
        if (pointCustomerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PointCustomerDTO result = pointCustomerService.save(pointCustomerDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, pointCustomerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /point-customers} : get all the pointCustomers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pointCustomers in body.
     */
    @GetMapping("/point-customers")
    public List<PointCustomerDTO> getAllPointCustomers() {
        log.debug("REST request to get all PointCustomers");
        return pointCustomerService.findAll();
    }

    /**
     * {@code GET  /point-customers/:id} : get the "id" pointCustomer.
     *
     * @param id the id of the pointCustomerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pointCustomerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/point-customers/{id}")
    public ResponseEntity<PointCustomerDTO> getPointCustomer(@PathVariable Long id) {
        log.debug("REST request to get PointCustomer : {}", id);
        Optional<PointCustomerDTO> pointCustomerDTO = pointCustomerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(pointCustomerDTO);
    }

    /**
     * {@code DELETE  /point-customers/:id} : delete the "id" pointCustomer.
     *
     * @param id the id of the pointCustomerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/point-customers/{id}")
    public ResponseEntity<Void> deletePointCustomer(@PathVariable Long id) {
        log.debug("REST request to delete PointCustomer : {}", id);
        pointCustomerService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
