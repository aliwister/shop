package com.badals.shop.web.rest;

import com.badals.shop.service.CartRuleService;
import com.badals.shop.web.rest.errors.BadRequestAlertException;
import com.badals.shop.service.dto.CartRuleDTO;

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
 * REST controller for managing {@link com.badals.shop.domain.CartRule}.
 */
@RestController
@RequestMapping("/api")
public class CartRuleResource {

    private final Logger log = LoggerFactory.getLogger(CartRuleResource.class);

    private static final String ENTITY_NAME = "cartRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CartRuleService cartRuleService;

    public CartRuleResource(CartRuleService cartRuleService) {
        this.cartRuleService = cartRuleService;
    }

    /**
     * {@code POST  /cart-rules} : Create a new cartRule.
     *
     * @param cartRuleDTO the cartRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cartRuleDTO, or with status {@code 400 (Bad Request)} if the cartRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cart-rules")
    public ResponseEntity<CartRuleDTO> createCartRule(@Valid @RequestBody CartRuleDTO cartRuleDTO) throws URISyntaxException {
        log.debug("REST request to save CartRule : {}", cartRuleDTO);
        if (cartRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new cartRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CartRuleDTO result = cartRuleService.save(cartRuleDTO);
        return ResponseEntity.created(new URI("/api/cart-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cart-rules} : Updates an existing cartRule.
     *
     * @param cartRuleDTO the cartRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cartRuleDTO,
     * or with status {@code 400 (Bad Request)} if the cartRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cartRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cart-rules")
    public ResponseEntity<CartRuleDTO> updateCartRule(@Valid @RequestBody CartRuleDTO cartRuleDTO) throws URISyntaxException {
        log.debug("REST request to update CartRule : {}", cartRuleDTO);
        if (cartRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CartRuleDTO result = cartRuleService.save(cartRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cartRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /cart-rules} : get all the cartRules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cartRules in body.
     */
    @GetMapping("/cart-rules")
    public List<CartRuleDTO> getAllCartRules() {
        log.debug("REST request to get all CartRules");
        return cartRuleService.findAll();
    }

    /**
     * {@code GET  /cart-rules/:id} : get the "id" cartRule.
     *
     * @param id the id of the cartRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cartRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cart-rules/{id}")
    public ResponseEntity<CartRuleDTO> getCartRule(@PathVariable Long id) {
        log.debug("REST request to get CartRule : {}", id);
        Optional<CartRuleDTO> cartRuleDTO = cartRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cartRuleDTO);
    }

    /**
     * {@code DELETE  /cart-rules/:id} : delete the "id" cartRule.
     *
     * @param id the id of the cartRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cart-rules/{id}")
    public ResponseEntity<Void> deleteCartRule(@PathVariable Long id) {
        log.debug("REST request to delete CartRule : {}", id);
        cartRuleService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
