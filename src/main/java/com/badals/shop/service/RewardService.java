package com.badals.shop.service;

import com.badals.shop.domain.Reward;
import com.badals.shop.repository.RewardRepository;
import com.badals.shop.service.dto.RewardDTO;
import com.badals.shop.service.mapper.RewardMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Reward}.
 */
@Service
@Transactional
public class RewardService {

    private final Logger log = LoggerFactory.getLogger(RewardService.class);

    private final RewardRepository rewardRepository;

    private final RewardMapper rewardMapper;

    public RewardService(RewardRepository rewardRepository, RewardMapper rewardMapper) {
        this.rewardRepository = rewardRepository;
        this.rewardMapper = rewardMapper;
    }

    /**
     * Save a reward.
     *
     * @param rewardDTO the entity to save.
     * @return the persisted entity.
     */
    public RewardDTO save(RewardDTO rewardDTO) {
        log.debug("Request to save Reward : {}", rewardDTO);
        Reward reward = rewardMapper.toEntity(rewardDTO);
        reward = rewardRepository.save(reward);
        return rewardMapper.toDto(reward);
    }

    /**
     * Get all the rewards.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RewardDTO> findAll() {
        log.debug("Request to get all Rewards");
        return rewardRepository.findAll().stream()
            .map(rewardMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one reward by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RewardDTO> findOne(Long id) {
        log.debug("Request to get Reward : {}", id);
        return rewardRepository.findById(id)
            .map(rewardMapper::toDto);
    }

    /**
     * Delete the reward by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Reward : {}", id);
        rewardRepository.deleteById(id);
    }
}
