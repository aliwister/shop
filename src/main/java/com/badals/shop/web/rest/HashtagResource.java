package com.badals.shop.web.rest;

import com.badals.shop.service.HashtagService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.HashtagDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.badals.shop.domain.Hashtag}.
 */
@RestController
@RequestMapping("/api")
public class HashtagResource {

    private final Logger log = LoggerFactory.getLogger(HashtagResource.class);

    private static final String ENTITY_NAME = "hashtag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HashtagService hashtagService;

    public HashtagResource(HashtagService hashtagService) {
        this.hashtagService = hashtagService;
    }

    /**
     * {@code POST  /hashtags} : Create a new hashtag.
     *
     * @param hashtagDTO the hashtagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hashtagDTO, or with status {@code 400 (Bad Request)} if the hashtag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/hashtags")
    public ResponseEntity<HashtagDTO> createHashtag(@RequestBody HashtagDTO hashtagDTO) throws URISyntaxException {
        log.debug("REST request to save Hashtag : {}", hashtagDTO);
        if (hashtagDTO.getId() != null) {
            throw new BadRequestAlertException("A new hashtag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HashtagDTO result = hashtagService.save(hashtagDTO);
        return ResponseEntity.created(new URI("/api/hashtags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /hashtags} : Updates an existing hashtag.
     *
     * @param hashtagDTO the hashtagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hashtagDTO,
     * or with status {@code 400 (Bad Request)} if the hashtagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hashtagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/hashtags")
    public ResponseEntity<HashtagDTO> updateHashtag(@RequestBody HashtagDTO hashtagDTO) throws URISyntaxException {
        log.debug("REST request to update Hashtag : {}", hashtagDTO);
        if (hashtagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        HashtagDTO result = hashtagService.save(hashtagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hashtagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /hashtags} : get all the hashtags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hashtags in body.
     */
    @GetMapping("/hashtags")
    public List<HashtagDTO> getAllHashtags() {
        log.debug("REST request to get all Hashtags");
        return hashtagService.findAll();
    }

    /**
     * {@code GET  /hashtags/:id} : get the "id" hashtag.
     *
     * @param id the id of the hashtagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hashtagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/hashtags/{id}")
    public ResponseEntity<HashtagDTO> getHashtag(@PathVariable Long id) {
        log.debug("REST request to get Hashtag : {}", id);
        Optional<HashtagDTO> hashtagDTO = hashtagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hashtagDTO);
    }

    /**
     * {@code DELETE  /hashtags/:id} : delete the "id" hashtag.
     *
     * @param id the id of the hashtagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/hashtags/{id}")
    public ResponseEntity<Void> deleteHashtag(@PathVariable Long id) {
        log.debug("REST request to delete Hashtag : {}", id);
        hashtagService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
