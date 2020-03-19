package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.PricingRequest;
import com.badals.shop.repository.PricingRequestRepository;
import com.badals.shop.service.PricingRequestService;
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
import java.util.List;

import static com.badals.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PricingRequestResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class PricingRequestResourceIT {

    private static final String DEFAULT_SKU = "AAAAAAAAAA";
    private static final String UPDATED_SKU = "BBBBBBBBBB";

    @Autowired
    private PricingRequestRepository pricingRequestRepository;

    @Autowired
    private PricingRequestService pricingRequestService;

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

    private MockMvc restPricingRequestMockMvc;

    private PricingRequest pricingRequest;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PricingRequestResource pricingRequestResource = new PricingRequestResource(pricingRequestService);
        this.restPricingRequestMockMvc = MockMvcBuilders.standaloneSetup(pricingRequestResource)
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
    public static PricingRequest createEntity(EntityManager em) {
        PricingRequest pricingRequest = new PricingRequest()
            .sku(DEFAULT_SKU);
        return pricingRequest;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PricingRequest createUpdatedEntity(EntityManager em) {
        PricingRequest pricingRequest = new PricingRequest()
            .sku(UPDATED_SKU);
        return pricingRequest;
    }

    @BeforeEach
    public void initTest() {
        pricingRequest = createEntity(em);
    }

    @Test
    @Transactional
    public void createPricingRequest() throws Exception {
        int databaseSizeBeforeCreate = pricingRequestRepository.findAll().size();

        // Create the PricingRequest
        restPricingRequestMockMvc.perform(post("/api/pricing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pricingRequest)))
            .andExpect(status().isCreated());

        // Validate the PricingRequest in the database
        List<PricingRequest> pricingRequestList = pricingRequestRepository.findAll();
        assertThat(pricingRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PricingRequest testPricingRequest = pricingRequestList.get(pricingRequestList.size() - 1);
        assertThat(testPricingRequest.getSku()).isEqualTo(DEFAULT_SKU);
    }

    @Test
    @Transactional
    public void createPricingRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pricingRequestRepository.findAll().size();

        // Create the PricingRequest with an existing ID
        pricingRequest.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPricingRequestMockMvc.perform(post("/api/pricing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pricingRequest)))
            .andExpect(status().isBadRequest());

        // Validate the PricingRequest in the database
        List<PricingRequest> pricingRequestList = pricingRequestRepository.findAll();
        assertThat(pricingRequestList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkSkuIsRequired() throws Exception {
        int databaseSizeBeforeTest = pricingRequestRepository.findAll().size();
        // set the field null
        pricingRequest.setSku(null);

        // Create the PricingRequest, which fails.

        restPricingRequestMockMvc.perform(post("/api/pricing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pricingRequest)))
            .andExpect(status().isBadRequest());

        List<PricingRequest> pricingRequestList = pricingRequestRepository.findAll();
        assertThat(pricingRequestList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPricingRequests() throws Exception {
        // Initialize the database
        pricingRequestRepository.saveAndFlush(pricingRequest);

        // Get all the pricingRequestList
        restPricingRequestMockMvc.perform(get("/api/pricing-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pricingRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].sku").value(hasItem(DEFAULT_SKU.toString())));
    }
    
    @Test
    @Transactional
    public void getPricingRequest() throws Exception {
        // Initialize the database
        pricingRequestRepository.saveAndFlush(pricingRequest);

        // Get the pricingRequest
        restPricingRequestMockMvc.perform(get("/api/pricing-requests/{id}", pricingRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pricingRequest.getId().intValue()))
            .andExpect(jsonPath("$.sku").value(DEFAULT_SKU.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPricingRequest() throws Exception {
        // Get the pricingRequest
        restPricingRequestMockMvc.perform(get("/api/pricing-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePricingRequest() throws Exception {
        // Initialize the database
        pricingRequestService.save(pricingRequest);

        int databaseSizeBeforeUpdate = pricingRequestRepository.findAll().size();

        // Update the pricingRequest
        PricingRequest updatedPricingRequest = pricingRequestRepository.findById(pricingRequest.getId()).get();
        // Disconnect from session so that the updates on updatedPricingRequest are not directly saved in db
        em.detach(updatedPricingRequest);
        updatedPricingRequest
            .sku(UPDATED_SKU);

        restPricingRequestMockMvc.perform(put("/api/pricing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPricingRequest)))
            .andExpect(status().isOk());

        // Validate the PricingRequest in the database
        List<PricingRequest> pricingRequestList = pricingRequestRepository.findAll();
        assertThat(pricingRequestList).hasSize(databaseSizeBeforeUpdate);
        PricingRequest testPricingRequest = pricingRequestList.get(pricingRequestList.size() - 1);
        assertThat(testPricingRequest.getSku()).isEqualTo(UPDATED_SKU);
    }

    @Test
    @Transactional
    public void updateNonExistingPricingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pricingRequestRepository.findAll().size();

        // Create the PricingRequest

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPricingRequestMockMvc.perform(put("/api/pricing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pricingRequest)))
            .andExpect(status().isBadRequest());

        // Validate the PricingRequest in the database
        List<PricingRequest> pricingRequestList = pricingRequestRepository.findAll();
        assertThat(pricingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePricingRequest() throws Exception {
        // Initialize the database
        pricingRequestService.save(pricingRequest);

        int databaseSizeBeforeDelete = pricingRequestRepository.findAll().size();

        // Delete the pricingRequest
        restPricingRequestMockMvc.perform(delete("/api/pricing-requests/{id}", pricingRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PricingRequest> pricingRequestList = pricingRequestRepository.findAll();
        assertThat(pricingRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PricingRequest.class);
        PricingRequest pricingRequest1 = new PricingRequest();
        pricingRequest1.setId(1L);
        PricingRequest pricingRequest2 = new PricingRequest();
        pricingRequest2.setId(pricingRequest1.getId());
        assertThat(pricingRequest1).isEqualTo(pricingRequest2);
        pricingRequest2.setId(2L);
        assertThat(pricingRequest1).isNotEqualTo(pricingRequest2);
        pricingRequest1.setId(null);
        assertThat(pricingRequest1).isNotEqualTo(pricingRequest2);
    }
}
