package com.badals.shop.web.rest;

import com.badals.shop.service.RewardService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.RewardDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.Reward}.
 */
@RestController
@RequestMapping("/api")
public class RewardResource {

    private final Logger log = LoggerFactory.getLogger(RewardResource.class);

    private static final String ENTITY_NAME = "reward";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RewardService rewardService;

    public RewardResource(RewardService rewardService) {
        this.rewardService = rewardService;
    }

    /**
     * {@code POST  /rewards} : Create a new reward.
     *
     * @param rewardDTO the rewardDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rewardDTO, or with status {@code 400 (Bad Request)} if the reward has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rewards")
    public ResponseEntity<RewardDTO> createReward(@Valid @RequestBody RewardDTO rewardDTO) throws URISyntaxException {
        log.debug("REST request to save Reward : {}", rewardDTO);
        if (rewardDTO.getId() != null) {
            throw new BadRequestAlertException("A new reward cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RewardDTO result = rewardService.save(rewardDTO);
        return ResponseEntity.created(new URI("/api/rewards/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rewards} : Updates an existing reward.
     *
     * @param rewardDTO the rewardDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rewardDTO,
     * or with status {@code 400 (Bad Request)} if the rewardDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rewardDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rewards")
    public ResponseEntity<RewardDTO> updateReward(@Valid @RequestBody RewardDTO rewardDTO) throws URISyntaxException {
        log.debug("REST request to update Reward : {}", rewardDTO);
        if (rewardDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RewardDTO result = rewardService.save(rewardDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rewardDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rewards} : get all the rewards.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rewards in body.
     */
    @GetMapping("/rewards")
    public List<RewardDTO> getAllRewards() {
        log.debug("REST request to get all Rewards");
        return rewardService.findAll();
    }

    /**
     * {@code GET  /rewards/:id} : get the "id" reward.
     *
     * @param id the id of the rewardDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rewardDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rewards/{id}")
    public ResponseEntity<RewardDTO> getReward(@PathVariable Long id) {
        log.debug("REST request to get Reward : {}", id);
        Optional<RewardDTO> rewardDTO = rewardService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rewardDTO);
    }

    /**
     * {@code DELETE  /rewards/:id} : delete the "id" reward.
     *
     * @param id the id of the rewardDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rewards/{id}")
    public ResponseEntity<Void> deleteReward(@PathVariable Long id) {
        log.debug("REST request to delete Reward : {}", id);
        rewardService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
