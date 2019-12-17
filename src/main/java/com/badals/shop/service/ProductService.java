package com.badals.shop.service;

import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductWrapper;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);

    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        ProductWrapper wrapper = new ProductWrapper();
        wrapper.setBody(product);
        //wrapper.setId(product.getId());
        wrapper.setParent(product.getParent());
        wrapper.setRewrite(product.getRewrite());
        wrapper.setIs_parent(false);
        wrapper.setSku(product.getBrand());
        wrapper.setViewCount(1);
        //wrapper.setUpdated(Calendar.);

        wrapper = productRepository.save(wrapper);
        return productMapper.toDto(wrapper.getBody());
    }

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.debug("Request to get all Products");
        return productRepository.findAll().stream()
            .map(e -> e.getBody())
            //.flatMap(productMapper.toDto(e->e.getBody()))
            .map(productMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id).map(e -> e.getBody())
            .map(productMapper::toDto);
    }

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }
}
