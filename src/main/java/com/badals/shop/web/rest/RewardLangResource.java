package com.badals.shop.web.rest;

import com.badals.shop.service.RewardLangService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.RewardLangDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.RewardLang}.
 */
@RestController
@RequestMapping("/api")
public class RewardLangResource {

    private final Logger log = LoggerFactory.getLogger(RewardLangResource.class);

    private static final String ENTITY_NAME = "rewardLang";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RewardLangService rewardLangService;

    public RewardLangResource(RewardLangService rewardLangService) {
        this.rewardLangService = rewardLangService;
    }

    /**
     * {@code POST  /reward-langs} : Create a new rewardLang.
     *
     * @param rewardLangDTO the rewardLangDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rewardLangDTO, or with status {@code 400 (Bad Request)} if the rewardLang has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reward-langs")
    public ResponseEntity<RewardLangDTO> createRewardLang(@Valid @RequestBody RewardLangDTO rewardLangDTO) throws URISyntaxException {
        log.debug("REST request to save RewardLang : {}", rewardLangDTO);
        if (rewardLangDTO.getId() != null) {
            throw new BadRequestAlertException("A new rewardLang cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RewardLangDTO result = rewardLangService.save(rewardLangDTO);
        return ResponseEntity.created(new URI("/api/reward-langs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reward-langs} : Updates an existing rewardLang.
     *
     * @param rewardLangDTO the rewardLangDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardLangDTO,
     * or with status {@code 400 (Bad Request)} if the rewardLangDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rewardLangDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reward-langs")
    public ResponseEntity<RewardLangDTO> updateRewardLang(@Valid @RequestBody RewardLangDTO rewardLangDTO) throws URISyntaxException {
        log.debug("REST request to update RewardLang : {}", rewardLangDTO);
        if (rewardLangDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RewardLangDTO result = rewardLangService.save(rewardLangDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardLangDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reward-langs} : get all the rewardLangs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rewardLangs in body.
     */
    @GetMapping("/reward-langs")
    public List<RewardLangDTO> getAllRewardLangs() {
        log.debug("REST request to get all RewardLangs");
        return rewardLangService.findAll();
    }

    /**
     * {@code GET  /reward-langs/:id} : get the "id" rewardLang.
     *
     * @param id the id of the rewardLangDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rewardLangDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reward-langs/{id}")
    public ResponseEntity<RewardLangDTO> getRewardLang(@PathVariable Long id) {
        log.debug("REST request to get RewardLang : {}", id);
        Optional<RewardLangDTO> rewardLangDTO = rewardLangService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rewardLangDTO);
    }

    /**
     * {@code DELETE  /reward-langs/:id} : delete the "id" rewardLang.
     *
     * @param id the id of the rewardLangDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reward-langs/{id}")
    public ResponseEntity<Void> deleteRewardLang(@PathVariable Long id) {
        log.debug("REST request to delete RewardLang : {}", id);
        rewardLangService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
