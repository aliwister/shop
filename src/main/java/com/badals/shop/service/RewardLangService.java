package com.badals.shop.service;

import com.badals.shop.domain.RewardLang;
import com.badals.shop.repository.RewardLangRepository;
import com.badals.shop.service.dto.RewardLangDTO;
import com.badals.shop.service.mapper.RewardLangMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link RewardLang}.
 */
@Service
@Transactional
public class RewardLangService {

    private final Logger log = LoggerFactory.getLogger(RewardLangService.class);

    private final RewardLangRepository rewardLangRepository;

    private final RewardLangMapper rewardLangMapper;

    public RewardLangService(RewardLangRepository rewardLangRepository, RewardLangMapper rewardLangMapper) {
        this.rewardLangRepository = rewardLangRepository;
        this.rewardLangMapper = rewardLangMapper;
    }

    /**
     * Save a rewardLang.
     *
     * @param rewardLangDTO the entity to save.
     * @return the persisted entity.
     */
    public RewardLangDTO save(RewardLangDTO rewardLangDTO) {
        log.debug("Request to save RewardLang : {}", rewardLangDTO);
        RewardLang rewardLang = rewardLangMapper.toEntity(rewardLangDTO);
        rewardLang = rewardLangRepository.save(rewardLang);
        return rewardLangMapper.toDto(rewardLang);
    }

    /**
     * Get all the rewardLangs.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RewardLangDTO> findAll() {
        log.debug("Request to get all RewardLangs");
        return rewardLangRepository.findAll().stream()
            .map(rewardLangMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one rewardLang by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RewardLangDTO> findOne(Long id) {
        log.debug("Request to get RewardLang : {}", id);
        return rewardLangRepository.findById(id)
            .map(rewardLangMapper::toDto);
    }

    /**
     * Delete the rewardLang by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete RewardLang : {}", id);
        rewardLangRepository.deleteById(id);
    }
}
