package com.badals.shop.web.rest;

import com.badals.shop.service.CarrierService;

import com.badals.shop.service.dto.CarrierDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.badals.shop.domain.Carrier}.
 */
@RestController
@RequestMapping("/api")
public class CarrierResource {

    private final Logger log = LoggerFactory.getLogger(CarrierResource.class);

    private final CarrierService carrierService;

    public CarrierResource(CarrierService carrierService) {
        this.carrierService = carrierService;
    }

    /**
     * {@code GET  /carriers} : get all the carriers.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of carriers in body.
     */
    @GetMapping("/carriers")
    public List<CarrierDTO> getAllCarriers() {
        log.debug("REST request to get all Carriers");
        return carrierService.findAll();
    }

    /**
     * {@code GET  /carriers/:id} : get the "id" carrier.
     *
     * @param id the id of the carrierDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the carrierDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/carriers/{id}")
    public ResponseEntity<CarrierDTO> getCarrier(@PathVariable Long id) {
        log.debug("REST request to get Carrier : {}", id);
        Optional<CarrierDTO> carrierDTO = carrierService.findOne(id);
        return ResponseUtil.wrapOrNotFound(carrierDTO);
    }
}
