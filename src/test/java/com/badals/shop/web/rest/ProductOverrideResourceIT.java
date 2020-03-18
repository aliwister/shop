package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.repository.ProductOverrideRepository;
import com.badals.shop.service.ProductOverrideService;
import com.badals.shop.service.dto.ProductOverrideDTO;
import com.badals.shop.service.mapper.ProductOverrideMapper;
import com.badals.shop.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static com.badals.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.badals.shop.domain.enumeration.OverrideType;
/**
 * Integration tests for the {@Link ProductOverrideResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class ProductOverrideResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    private static final OverrideType DEFAULT_TYPE = OverrideType.COST;
    private static final OverrideType UPDATED_TYPE = OverrideType.WEIGHT;

    private static final String DEFAULT_OVERRIDE = "AAAAAAAAAA";
    private static final String UPDATED_OVERRIDE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final Boolean DEFAULT_LAZY = false;
    private static final Boolean UPDATED_LAZY = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private ProductOverrideRepository productOverrideRepository;

    @Autowired
    private ProductOverrideMapper productOverrideMapper;

    @Autowired
    private ProductOverrideService productOverrideService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restProductOverrideMockMvc;

    private ProductOverride productOverride;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ProductOverrideResource productOverrideResource = new ProductOverrideResource(productOverrideService);
        this.restProductOverrideMockMvc = MockMvcBuilders.standaloneSetup(productOverrideResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOverride createEntity(EntityManager em) {
        ProductOverride productOverride = new ProductOverride()
            .sku(DEFAULT_SKU)
            .type(DEFAULT_TYPE)
            .override(DEFAULT_OVERRIDE)
            .active(DEFAULT_ACTIVE)
            .lazy(DEFAULT_LAZY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
        return productOverride;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProductOverride createUpdatedEntity(EntityManager em) {
        ProductOverride productOverride = new ProductOverride()
            .sku(UPDATED_SKU)
            .type(UPDATED_TYPE)
            .override(UPDATED_OVERRIDE)
            .active(UPDATED_ACTIVE)
            .lazy(UPDATED_LAZY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        return productOverride;
    }

    @BeforeEach
    public void initTest() {
        productOverride = createEntity(em);
    }

    @Test
    @Transactional
    public void createProductOverride() throws Exception {
        int databaseSizeBeforeCreate = productOverrideRepository.findAll().size();

        // Create the ProductOverride
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);
        restProductOverrideMockMvc.perform(post("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isCreated());

        // Validate the ProductOverride in the database
        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeCreate + 1);
        ProductOverride testProductOverride = productOverrideList.get(productOverrideList.size() - 1);
        assertThat(testProductOverride.getSku()).isEqualTo(DEFAULT_SKU);
        assertThat(testProductOverride.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testProductOverride.getOverride()).isEqualTo(DEFAULT_OVERRIDE);
        assertThat(testProductOverride.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testProductOverride.isLazy()).isEqualTo(DEFAULT_LAZY);
        assertThat(testProductOverride.getCreatedDate()).isEqualTo(DEFAULT_CREATED_DATE);
        assertThat(testProductOverride.getLastModifiedDate()).isEqualTo(DEFAULT_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void createProductOverrideWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = productOverrideRepository.findAll().size();

        // Create the ProductOverride with an existing ID
        productOverride.setId(1L);
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);

        // An entity with an existing ID cannot be created, so this API call must fail
        restProductOverrideMockMvc.perform(post("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOverride in the database
        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOverrideRepository.findAll().size();
        // set the field null
        productOverride.setSku(null);

        // Create the ProductOverride, which fails.
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);

        restProductOverrideMockMvc.perform(post("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isBadRequest());

        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOverrideRepository.findAll().size();
        // set the field null
        productOverride.setType(null);

        // Create the ProductOverride, which fails.
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);

        restProductOverrideMockMvc.perform(post("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isBadRequest());

        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOverrideRepository.findAll().size();
        // set the field null
        productOverride.setActive(null);

        // Create the ProductOverride, which fails.
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);

        restProductOverrideMockMvc.perform(post("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isBadRequest());

        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLazyIsRequired() throws Exception {
        int databaseSizeBeforeTest = productOverrideRepository.findAll().size();
        // set the field null
        productOverride.setLazy(null);

        // Create the ProductOverride, which fails.
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);

        restProductOverrideMockMvc.perform(post("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isBadRequest());

        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllProductOverrides() throws Exception {
        // Initialize the database
        productOverrideRepository.saveAndFlush(productOverride);

        // Get all the productOverrideList
        restProductOverrideMockMvc.perform(get("/api/product-overrides?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(productOverride.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].override").value(hasItem(DEFAULT_OVERRIDE.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].lazy").value(hasItem(DEFAULT_LAZY.booleanValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getProductOverride() throws Exception {
        // Initialize the database
        productOverrideRepository.saveAndFlush(productOverride);

        // Get the productOverride
        restProductOverrideMockMvc.perform(get("/api/product-overrides/{id}", productOverride.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(productOverride.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.override").value(DEFAULT_OVERRIDE.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.lazy").value(DEFAULT_LAZY.booleanValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductOverride() throws Exception {
        // Get the productOverride
        restProductOverrideMockMvc.perform(get("/api/product-overrides/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductOverride() throws Exception {
        // Initialize the database
        productOverrideRepository.saveAndFlush(productOverride);

        int databaseSizeBeforeUpdate = productOverrideRepository.findAll().size();

        // Update the productOverride
        ProductOverride updatedProductOverride = productOverrideRepository.findById(productOverride.getId()).get();
        // Disconnect from session so that the updates on updatedProductOverride are not directly saved in db
        em.detach(updatedProductOverride);
        updatedProductOverride
            .sku(UPDATED_SKU)
            .type(UPDATED_TYPE)
            .override(UPDATED_OVERRIDE)
            .active(UPDATED_ACTIVE)
            .lazy(UPDATED_LAZY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(updatedProductOverride);

        restProductOverrideMockMvc.perform(put("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isOk());

        // Validate the ProductOverride in the database
        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeUpdate);
        ProductOverride testProductOverride = productOverrideList.get(productOverrideList.size() - 1);
        assertThat(testProductOverride.getSku()).isEqualTo(UPDATED_SKU);
        assertThat(testProductOverride.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testProductOverride.getOverride()).isEqualTo(UPDATED_OVERRIDE);
        assertThat(testProductOverride.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testProductOverride.isLazy()).isEqualTo(UPDATED_LAZY);
        assertThat(testProductOverride.getCreatedDate()).isEqualTo(UPDATED_CREATED_DATE);
        assertThat(testProductOverride.getLastModifiedDate()).isEqualTo(UPDATED_LAST_MODIFIED_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingProductOverride() throws Exception {
        int databaseSizeBeforeUpdate = productOverrideRepository.findAll().size();

        // Create the ProductOverride
        ProductOverrideDTO productOverrideDTO = productOverrideMapper.toDto(productOverride);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProductOverrideMockMvc.perform(put("/api/product-overrides")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(productOverrideDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProductOverride in the database
        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteProductOverride() throws Exception {
        // Initialize the database
        productOverrideRepository.saveAndFlush(productOverride);

        int databaseSizeBeforeDelete = productOverrideRepository.findAll().size();

        // Delete the productOverride
        restProductOverrideMockMvc.perform(delete("/api/product-overrides/{id}", productOverride.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ProductOverride> productOverrideList = productOverrideRepository.findAll();
        assertThat(productOverrideList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOverride.class);
        ProductOverride productOverride1 = new ProductOverride();
        productOverride1.setId(1L);
        ProductOverride productOverride2 = new ProductOverride();
        productOverride2.setId(productOverride1.getId());
        assertThat(productOverride1).isEqualTo(productOverride2);
        productOverride2.setId(2L);
        assertThat(productOverride1).isNotEqualTo(productOverride2);
        productOverride1.setId(null);
        assertThat(productOverride1).isNotEqualTo(productOverride2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProductOverrideDTO.class);
        ProductOverrideDTO productOverrideDTO1 = new ProductOverrideDTO();
        productOverrideDTO1.setId(1L);
        ProductOverrideDTO productOverrideDTO2 = new ProductOverrideDTO();
        assertThat(productOverrideDTO1).isNotEqualTo(productOverrideDTO2);
        productOverrideDTO2.setId(productOverrideDTO1.getId());
        assertThat(productOverrideDTO1).isEqualTo(productOverrideDTO2);
        productOverrideDTO2.setId(2L);
        assertThat(productOverrideDTO1).isNotEqualTo(productOverrideDTO2);
        productOverrideDTO1.setId(null);
        assertThat(productOverrideDTO1).isNotEqualTo(productOverrideDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(productOverrideMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(productOverrideMapper.fromId(null)).isNull();
    }
}
