package com.badals.shop.service;

import com.algolia.search.SearchIndex;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.ProductI18;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.repository.ProductLangRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.domain.AlgoliaProduct;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.AlgoliaProductMapper;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    private final AlgoliaProductMapper algoliaProductMapper;

    private final SearchIndex<AlgoliaProduct> index;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, AlgoliaProductMapper algoliaProductMapper, SearchIndex<AlgoliaProduct> index) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.algoliaProductMapper = algoliaProductMapper;
        this.index = index;
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
        product = productRepository.save(product);
        return productMapper.toDto(product);
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
        return productRepository.findById(id)
            .map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findOneEntity(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
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

    //@PreAuthorize("isAuthenticated()")
    public Optional<ProductDTO> getProduct(int id) {
        return this.findOne((long) id);
    }


    public Optional<ProductDTO> getProductAdmin(int id) {
        return this.findOne((long) id);
    }

    public List<ProductDTO> getAllProducts(int count) {
        return this.findAll();
    }

    public ProductDTO createProduct(Long ref, Long parent, String sku, String upc, LocalDate releaseDate) {
        Product product = new Product();
        product.ref(ref).sku(sku).upc(upc).releaseDate(releaseDate);
        return productMapper.toDto(product);
    }

    public ProductDTO createNewProduct(ProductDTO product) {
        ProductDTO product2 = save(product);

        return product2;
    }

    @Autowired
    ProductLangRepository productLangRepository;

    public Attribute indexProduct(long id) {
        Product product = productRepository.getOne(id);
        AlgoliaProduct algoliaProduct = algoliaProductMapper.producttoAlgoliaProduct(product);

        for(ProductLang i: productLangRepository.findAllByProductId( id ) ){
            algoliaProduct.getI18().put(i.getLang(), new ProductI18(i.getTitle(), "", ""));
        }
        index.saveObject(algoliaProduct);
        return new Attribute("success", "1");
    }

    public ProductDTO getProductBySlug(String slug) throws ProductNotFoundException {
        Product product = productRepository.findBySlugJoinCategories(slug).get();
        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() <1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().iterator().next();
        }


        return productRepository.findBySlugJoinCategories(product.getSlug()).map(productMapper::toDto).orElse(null);
        //return productRepository.findBySlugJoinCategories(slug).map(productMapper::toDto).orElse(null);
    }

    public ProductResponse findAllByCategory(String slug, Integer offset, Integer limit) {
        List<Product> products = productRepository.findAllByCategorySlug(slug);
        ProductResponse response = new ProductResponse();
        response.setTotal(products.size());
        response.setItems(products.stream().map(productMapper::toDto).collect(Collectors.toList()));
        return response;
    }
    public List<ProductDTO> findRelated(String slug) {
        List<Product> products = productRepository.findAllByCategorySlug(slug);

        return products.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    public ProductDTO getProductBySku(String sku) throws ProductNotFoundException {
        Product product = productRepository.findBySkuJoinCategories( sku).get();
        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() <1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().iterator().next();
        }


        return productRepository.findBySlugJoinCategories(product.getSlug()).map(productMapper::toDto).orElse(null);

    }
}
