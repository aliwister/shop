package com.badals.shop.service;

import com.badals.shop.domain.Action;
import com.badals.shop.repository.ActionRepository;
import com.badals.shop.repository.ActionRepository;
import com.badals.shop.service.dto.ActionDTO;
import com.badals.shop.service.mapper.ActionMapper;
import com.badals.shop.service.mapper.ActionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Action}.
 */
@Service
@Transactional
public class ActionService {

    private final Logger log = LoggerFactory.getLogger(ActionService.class);

    private final ActionRepository actionRepository;

    private final ActionMapper actionMapper;

    public ActionService(ActionRepository actionRepository, ActionMapper actionMapper) {
        this.actionRepository = actionRepository;
        this.actionMapper = actionMapper;
    }

    /**
     * Save a action.
     *
     * @param actionDTO the entity to save.
     * @return the persisted entity.
     */
    public ActionDTO save(ActionDTO actionDTO) {
        log.debug("Request to save Action : {}", actionDTO);
        Action action = actionMapper.toEntity(actionDTO);
        action = actionRepository.save(action);
        return actionMapper.toDto(action);
    }

    /**
     * Get all the actions.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ActionDTO> findAll() {
        log.debug("Request to get all Actions");
        return actionRepository.findAll().stream()
            .map(actionMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one action by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ActionDTO> findOne(Long id) {
        log.debug("Request to get Action : {}", id);
        return actionRepository.findById(id)
            .map(actionMapper::toDto);
    }

    /**
     * Delete the action by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Action : {}", id);
        actionRepository.deleteById(id);
    }

    public List<ActionDTO> auditActivity(Long id, String type) {
        return actionRepository.findAllByObjectIdAndObject(String.valueOf(id), type).stream().map(actionMapper::toDto).collect(Collectors.toList());
    }
}
