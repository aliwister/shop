package com.badals.shop.web.rest;

import com.badals.shop.service.SpeedDialService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.SpeedDialDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.SpeedDial}.
 */
@RestController
@RequestMapping("/api")
public class SpeedDialResource {

    private final Logger log = LoggerFactory.getLogger(SpeedDialResource.class);

    private static final String ENTITY_NAME = "speedDial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpeedDialService speedDialService;

    public SpeedDialResource(SpeedDialService speedDialService) {
        this.speedDialService = speedDialService;
    }

    /**
     * {@code POST  /speed-dials} : Create a new speedDial.
     *
     * @param speedDialDTO the speedDialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new speedDialDTO, or with status {@code 400 (Bad Request)} if the speedDial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/speed-dials")
    public ResponseEntity<SpeedDialDTO> createSpeedDial(@Valid @RequestBody SpeedDialDTO speedDialDTO) throws URISyntaxException {
        log.debug("REST request to save SpeedDial : {}", speedDialDTO);
        if (speedDialDTO.getId() != null) {
            throw new BadRequestAlertException("A new speedDial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SpeedDialDTO result = speedDialService.save(speedDialDTO);
        return ResponseEntity.created(new URI("/api/speed-dials/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /speed-dials} : Updates an existing speedDial.
     *
     * @param speedDialDTO the speedDialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated speedDialDTO,
     * or with status {@code 400 (Bad Request)} if the speedDialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the speedDialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/speed-dials")
    public ResponseEntity<SpeedDialDTO> updateSpeedDial(@Valid @RequestBody SpeedDialDTO speedDialDTO) throws URISyntaxException {
        log.debug("REST request to update SpeedDial : {}", speedDialDTO);
        if (speedDialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SpeedDialDTO result = speedDialService.save(speedDialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, speedDialDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /speed-dials} : get all the speedDials.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of speedDials in body.
     */
    @GetMapping("/speed-dials")
    public List<SpeedDialDTO> getAllSpeedDials() {
        log.debug("REST request to get all SpeedDials");
        return speedDialService.findAll();
    }

    /**
     * {@code GET  /speed-dials/:id} : get the "id" speedDial.
     *
     * @param id the id of the speedDialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the speedDialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/speed-dials/{id}")
    public ResponseEntity<SpeedDialDTO> getSpeedDial(@PathVariable Long id) {
        log.debug("REST request to get SpeedDial : {}", id);
        Optional<SpeedDialDTO> speedDialDTO = speedDialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(speedDialDTO);
    }

    /**
     * {@code DELETE  /speed-dials/:id} : delete the "id" speedDial.
     *
     * @param id the id of the speedDialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/speed-dials/{id}")
    public ResponseEntity<Void> deleteSpeedDial(@PathVariable Long id) {
        log.debug("REST request to delete SpeedDial : {}", id);
        speedDialService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
