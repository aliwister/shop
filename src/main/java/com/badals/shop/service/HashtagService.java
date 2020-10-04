package com.badals.shop.service;

import com.badals.shop.domain.Hashtag;
import com.badals.shop.repository.HashtagRepository;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.mapper.HashtagMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Hashtag}.
 */
@Service
@Transactional
public class HashtagService {

    private final Logger log = LoggerFactory.getLogger(HashtagService.class);

    private final HashtagRepository hashtagRepository;

    private final HashtagMapper hashtagMapper;

    public HashtagService(HashtagRepository hashtagRepository, HashtagMapper hashtagMapper) {
        this.hashtagRepository = hashtagRepository;
        this.hashtagMapper = hashtagMapper;
    }

    /**
     * Save a hashtag.
     *
     * @param hashtagDTO the entity to save.
     * @return the persisted entity.
     */
    public HashtagDTO save(HashtagDTO hashtagDTO) {
        log.debug("Request to save Hashtag : {}", hashtagDTO);
        Hashtag hashtag = hashtagMapper.toEntity(hashtagDTO);
        hashtag = hashtagRepository.save(hashtag);
        return hashtagMapper.toDto(hashtag);
    }

    /**
     * Get all the hashtags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<HashtagDTO> findAll() {
        log.debug("Request to get all Hashtags");
        return hashtagRepository.findAll().stream()
            .map(hashtagMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one hashtag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<HashtagDTO> findOne(Long id) {
        log.debug("Request to get Hashtag : {}", id);
        return hashtagRepository.findById(id)
            .map(hashtagMapper::toDto);
    }

    /**
     * Delete the hashtag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Hashtag : {}", id);
        hashtagRepository.deleteById(id);
    }
}
