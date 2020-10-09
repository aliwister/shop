package com.badals.shop.service;

import com.badals.shop.domain.Hashtag;
import com.badals.shop.domain.pojo.HashtagResponse;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.repository.HashtagRepository;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.mapper.HashtagMapper;
import com.badals.shop.service.pojo.AddProductDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    private final ProductService productService;

    private final HashtagMapper hashtagMapper;

    public HashtagService(HashtagRepository hashtagRepository, ProductService productService, HashtagMapper hashtagMapper) {
        this.hashtagRepository = hashtagRepository;
        this.productService = productService;
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

    public HashtagResponse findForList(Integer offset,Integer limit ) {
        Page<Hashtag> orders = hashtagRepository.findForList(/*orderState, */PageRequest.of((int) offset/limit,limit));
        HashtagResponse response = new HashtagResponse();
        response.setTotal(orders.getNumber());
        response.setItems(orders.getContent().stream().map(hashtagMapper::toDto).collect(Collectors.toList()));
        response.setHasMore(orders.hasNext());
        return response;

    }
    public static final String LATEST = "LATEST";
    @Cacheable(cacheNames = LATEST)
    public HashtagResponse findForListWithProducts(Integer offset, Integer limit) {
        Page<Hashtag> tags = hashtagRepository.findForList(/*orderState, */PageRequest.of((int) offset/limit,limit));
        HashtagResponse response = new HashtagResponse();
        response.setTotal(tags.getNumber());
        response.setItems(tags.getContent().stream().map(hashtagMapper::toDto).collect(Collectors.toList()));
        response.setHasMore(tags.hasNext());

        response.getItems().forEach(x ->{
            x.setProducts(productService.findByHashtag(x.getEn()));
        });

        return response;
    }

    public HashtagResponse findRelatedTo(Long ref, List<String> hashtags, String title) {
        List<Hashtag> tags = hashtagRepository.findByEnIn(hashtags);
        HashtagResponse response = new HashtagResponse();
        response.setItems(tags.stream().map(hashtagMapper::toDto).collect(Collectors.toList()));
        response.setHasMore(false);

        response.getItems().forEach(x ->{
            x.setProducts(productService.findByHashtag(x.getEn() + " AND !(ref:" + ref + ")"));
        });

        return response;
    }

}
