package com.badals.shop.service;

import com.badals.shop.domain.CartRule;
import com.badals.shop.repository.CartRuleRepository;
import com.badals.shop.service.dto.CartRuleDTO;
import com.badals.shop.service.mapper.CartRuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link CartRule}.
 */
@Service
@Transactional
public class CartRuleService {

    private final Logger log = LoggerFactory.getLogger(CartRuleService.class);

    private final CartRuleRepository cartRuleRepository;

    private final CartRuleMapper cartRuleMapper;

    public CartRuleService(CartRuleRepository cartRuleRepository, CartRuleMapper cartRuleMapper) {
        this.cartRuleRepository = cartRuleRepository;
        this.cartRuleMapper = cartRuleMapper;
    }

    /**
     * Save a cartRule.
     *
     * @param cartRuleDTO the entity to save.
     * @return the persisted entity.
     */
    public CartRuleDTO save(CartRuleDTO cartRuleDTO) {
        log.debug("Request to save CartRule : {}", cartRuleDTO);
        CartRule cartRule = cartRuleMapper.toEntity(cartRuleDTO);
        cartRule = cartRuleRepository.save(cartRule);
        return cartRuleMapper.toDto(cartRule);
    }

    /**
     * Get all the cartRules.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CartRuleDTO> findAll() {
        log.debug("Request to get all CartRules");
        return cartRuleRepository.findAll().stream()
            .map(cartRuleMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one cartRule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CartRuleDTO> findOne(Long id) {
        log.debug("Request to get CartRule : {}", id);
        return cartRuleRepository.findById(id)
            .map(cartRuleMapper::toDto);
    }

    /**
     * Delete the cartRule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete CartRule : {}", id);
        cartRuleRepository.deleteById(id);
    }
}
