package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.Carrier;
import com.badals.shop.repository.CarrierRepository;
import com.badals.shop.service.CarrierService;
import com.badals.shop.service.mapper.CarrierMapper;
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
import java.math.BigDecimal;

import static com.badals.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CarrierResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class CarrierResourceIT {

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MAX_WEIGHT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MAX_WEIGHT = new BigDecimal(2);

    @Autowired
    private CarrierRepository carrierRepository;

    @Autowired
    private CarrierMapper carrierMapper;

    @Autowired
    private CarrierService carrierService;

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

    private MockMvc restCarrierMockMvc;

    private Carrier carrier;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CarrierResource carrierResource = new CarrierResource(carrierService);
        this.restCarrierMockMvc = MockMvcBuilders.standaloneSetup(carrierResource)
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
    public static Carrier createEntity(EntityManager em) {
        Carrier carrier = new Carrier()
            .ref(DEFAULT_REF)
            .name(DEFAULT_NAME)
            .maxWeight(DEFAULT_MAX_WEIGHT);
        return carrier;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Carrier createUpdatedEntity(EntityManager em) {
        Carrier carrier = new Carrier()
            .ref(UPDATED_REF)
            .name(UPDATED_NAME)
            .maxWeight(UPDATED_MAX_WEIGHT);
        return carrier;
    }

    @BeforeEach
    public void initTest() {
        carrier = createEntity(em);
    }

    @Test
    @Transactional
    public void getAllCarriers() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);

        // Get all the carrierList
        restCarrierMockMvc.perform(get("/api/carriers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(carrier.getId().intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].maxWeight").value(hasItem(DEFAULT_MAX_WEIGHT.intValue())));
    }

    @Test
    @Transactional
    public void getCarrier() throws Exception {
        // Initialize the database
        carrierRepository.saveAndFlush(carrier);

        // Get the carrier
        restCarrierMockMvc.perform(get("/api/carriers/{id}", carrier.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carrier.getId().intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.maxWeight").value(DEFAULT_MAX_WEIGHT.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCarrier() throws Exception {
        // Get the carrier
        restCarrierMockMvc.perform(get("/api/carriers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }
}
