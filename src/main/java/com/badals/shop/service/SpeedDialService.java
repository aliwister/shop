package com.badals.shop.service;

import com.badals.shop.domain.SpeedDial;
import com.badals.shop.repository.SpeedDialRepository;
import com.badals.shop.service.dto.SpeedDialDTO;
import com.badals.shop.service.mapper.SpeedDialMapper;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link SpeedDial}.
 */
@Service
@Transactional
public class SpeedDialService {

    private final Logger log = LoggerFactory.getLogger(SpeedDialService.class);

    private final SpeedDialRepository speedDialRepository;

    private final SpeedDialMapper speedDialMapper;

    public SpeedDialService(SpeedDialRepository speedDialRepository, SpeedDialMapper speedDialMapper) {
        this.speedDialRepository = speedDialRepository;
        this.speedDialMapper = speedDialMapper;
    }

    /**
     * Save a speedDial.
     *
     * @param speedDialDTO the entity to save.
     * @return the persisted entity.
     */
    public SpeedDialDTO save(SpeedDialDTO speedDialDTO) {
        log.debug("Request to save SpeedDial : {}", speedDialDTO);
        SpeedDial speedDial = speedDialMapper.toEntity(speedDialDTO);
        speedDial = speedDialRepository.save(speedDial);
        return speedDialMapper.toDto(speedDial);
    }

    /**
     * Get all the speedDials.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<SpeedDialDTO> findAll() {
        log.debug("Request to get all SpeedDials");
        return speedDialRepository.findAll().stream()
            .map(speedDialMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one speedDial by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SpeedDialDTO> findOne(Long id) {
        log.debug("Request to get SpeedDial : {}", id);
        return speedDialRepository.findById(id)
            .map(speedDialMapper::toDto);
    }

    /**
     * Delete the speedDial by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete SpeedDial : {}", id);
        speedDialRepository.deleteById(id);
    }

    public Long findRefByDial(String dial) throws ProductNotFoundException {
        SpeedDial ret = speedDialRepository.findByDial(dial).orElse(null);
        if(ret == null) {
            throw new ProductNotFoundException("Invalid Speed dial");
        }
        return ret.getRef();
    }

   public void addDial(String dial, Long ref) {
       SpeedDial d = speedDialRepository.findByDial(dial).orElse(new SpeedDial());
       d.setRef(ref);
       d.setDial(dial);
       d.setExpires(Instant.now());
       speedDialRepository.save(d);
   }
}
