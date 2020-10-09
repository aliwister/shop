package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.PointCustomer;
import com.badals.shop.repository.PointCustomerRepository;
import com.badals.shop.service.PointCustomerService;
import com.badals.shop.service.dto.PointCustomerDTO;
import com.badals.shop.service.mapper.PointCustomerMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.badals.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PointCustomerResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class PointCustomerResourceIT {

    private static final Long DEFAULT_TOTAL_POINTS = 1L;
    private static final Long UPDATED_TOTAL_POINTS = 2L;

    private static final Long DEFAULT_SPENT_POINTS = 1L;
    private static final Long UPDATED_SPENT_POINTS = 2L;

    private static final String DEFAULT_REFERRAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_REFERRAL_CODE = "BBBBBBBBBB";

    private static final Double DEFAULT_SPENT_MONEY = 1D;
    private static final Double UPDATED_SPENT_MONEY = 2D;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final LocalDate DEFAULT_DATE_ADD = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_ADD = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private PointCustomerRepository pointCustomerRepository;

    @Autowired
    private PointCustomerMapper pointCustomerMapper;

    @Autowired
    private PointCustomerService pointCustomerService;

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

    private MockMvc restPointCustomerMockMvc;

    private PointCustomer pointCustomer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PointCustomerResource pointCustomerResource = new PointCustomerResource(pointCustomerService);
        this.restPointCustomerMockMvc = MockMvcBuilders.standaloneSetup(pointCustomerResource)
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
    public static PointCustomer createEntity(EntityManager em) {
        PointCustomer pointCustomer = new PointCustomer()
            .totalPoints(DEFAULT_TOTAL_POINTS)
            .spentPoints(DEFAULT_SPENT_POINTS)
            .referralCode(DEFAULT_REFERRAL_CODE)
            .spentMoney(DEFAULT_SPENT_MONEY)
            .active(DEFAULT_ACTIVE)
            .date_add(DEFAULT_DATE_ADD);
        return pointCustomer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PointCustomer createUpdatedEntity(EntityManager em) {
        PointCustomer pointCustomer = new PointCustomer()
            .totalPoints(UPDATED_TOTAL_POINTS)
            .spentPoints(UPDATED_SPENT_POINTS)
            .referralCode(UPDATED_REFERRAL_CODE)
            .spentMoney(UPDATED_SPENT_MONEY)
            .active(UPDATED_ACTIVE)
            .date_add(UPDATED_DATE_ADD);
        return pointCustomer;
    }

    @BeforeEach
    public void initTest() {
        pointCustomer = createEntity(em);
    }

    @Test
    @Transactional
    public void createPointCustomer() throws Exception {
        int databaseSizeBeforeCreate = pointCustomerRepository.findAll().size();

        // Create the PointCustomer
        PointCustomerDTO pointCustomerDTO = pointCustomerMapper.toDto(pointCustomer);
        restPointCustomerMockMvc.perform(post("/api/point-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointCustomerDTO)))
            .andExpect(status().isCreated());

        // Validate the PointCustomer in the database
        List<PointCustomer> pointCustomerList = pointCustomerRepository.findAll();
        assertThat(pointCustomerList).hasSize(databaseSizeBeforeCreate + 1);
        PointCustomer testPointCustomer = pointCustomerList.get(pointCustomerList.size() - 1);
        assertThat(testPointCustomer.getTotalPoints()).isEqualTo(DEFAULT_TOTAL_POINTS);
        assertThat(testPointCustomer.getSpentPoints()).isEqualTo(DEFAULT_SPENT_POINTS);
        assertThat(testPointCustomer.getReferralCode()).isEqualTo(DEFAULT_REFERRAL_CODE);
        assertThat(testPointCustomer.getSpentMoney()).isEqualTo(DEFAULT_SPENT_MONEY);
        assertThat(testPointCustomer.isActive()).isEqualTo(DEFAULT_ACTIVE);
        assertThat(testPointCustomer.getDate_add()).isEqualTo(DEFAULT_DATE_ADD);
    }

    @Test
    @Transactional
    public void createPointCustomerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pointCustomerRepository.findAll().size();

        // Create the PointCustomer with an existing ID
        pointCustomer.setId(1L);
        PointCustomerDTO pointCustomerDTO = pointCustomerMapper.toDto(pointCustomer);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPointCustomerMockMvc.perform(post("/api/point-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointCustomerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PointCustomer in the database
        List<PointCustomer> pointCustomerList = pointCustomerRepository.findAll();
        assertThat(pointCustomerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = pointCustomerRepository.findAll().size();
        // set the field null
        pointCustomer.setActive(null);

        // Create the PointCustomer, which fails.
        PointCustomerDTO pointCustomerDTO = pointCustomerMapper.toDto(pointCustomer);

        restPointCustomerMockMvc.perform(post("/api/point-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointCustomerDTO)))
            .andExpect(status().isBadRequest());

        List<PointCustomer> pointCustomerList = pointCustomerRepository.findAll();
        assertThat(pointCustomerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPointCustomers() throws Exception {
        // Initialize the database
        pointCustomerRepository.saveAndFlush(pointCustomer);

        // Get all the pointCustomerList
        restPointCustomerMockMvc.perform(get("/api/point-customers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pointCustomer.getId().intValue())))
            .andExpect(jsonPath("$.[*].totalPoints").value(hasItem(DEFAULT_TOTAL_POINTS.intValue())))
            .andExpect(jsonPath("$.[*].spentPoints").value(hasItem(DEFAULT_SPENT_POINTS.intValue())))
            .andExpect(jsonPath("$.[*].referralCode").value(hasItem(DEFAULT_REFERRAL_CODE.toString())))
            .andExpect(jsonPath("$.[*].spentMoney").value(hasItem(DEFAULT_SPENT_MONEY.doubleValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].date_add").value(hasItem(DEFAULT_DATE_ADD.toString())));
    }
    
    @Test
    @Transactional
    public void getPointCustomer() throws Exception {
        // Initialize the database
        pointCustomerRepository.saveAndFlush(pointCustomer);

        // Get the pointCustomer
        restPointCustomerMockMvc.perform(get("/api/point-customers/{id}", pointCustomer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pointCustomer.getId().intValue()))
            .andExpect(jsonPath("$.totalPoints").value(DEFAULT_TOTAL_POINTS.intValue()))
            .andExpect(jsonPath("$.spentPoints").value(DEFAULT_SPENT_POINTS.intValue()))
            .andExpect(jsonPath("$.referralCode").value(DEFAULT_REFERRAL_CODE.toString()))
            .andExpect(jsonPath("$.spentMoney").value(DEFAULT_SPENT_MONEY.doubleValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.date_add").value(DEFAULT_DATE_ADD.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPointCustomer() throws Exception {
        // Get the pointCustomer
        restPointCustomerMockMvc.perform(get("/api/point-customers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePointCustomer() throws Exception {
        // Initialize the database
        pointCustomerRepository.saveAndFlush(pointCustomer);

        int databaseSizeBeforeUpdate = pointCustomerRepository.findAll().size();

        // Update the pointCustomer
        PointCustomer updatedPointCustomer = pointCustomerRepository.findById(pointCustomer.getId()).get();
        // Disconnect from session so that the updates on updatedPointCustomer are not directly saved in db
        em.detach(updatedPointCustomer);
        updatedPointCustomer
            .totalPoints(UPDATED_TOTAL_POINTS)
            .spentPoints(UPDATED_SPENT_POINTS)
            .referralCode(UPDATED_REFERRAL_CODE)
            .spentMoney(UPDATED_SPENT_MONEY)
            .active(UPDATED_ACTIVE)
            .date_add(UPDATED_DATE_ADD);
        PointCustomerDTO pointCustomerDTO = pointCustomerMapper.toDto(updatedPointCustomer);

        restPointCustomerMockMvc.perform(put("/api/point-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointCustomerDTO)))
            .andExpect(status().isOk());

        // Validate the PointCustomer in the database
        List<PointCustomer> pointCustomerList = pointCustomerRepository.findAll();
        assertThat(pointCustomerList).hasSize(databaseSizeBeforeUpdate);
        PointCustomer testPointCustomer = pointCustomerList.get(pointCustomerList.size() - 1);
        assertThat(testPointCustomer.getTotalPoints()).isEqualTo(UPDATED_TOTAL_POINTS);
        assertThat(testPointCustomer.getSpentPoints()).isEqualTo(UPDATED_SPENT_POINTS);
        assertThat(testPointCustomer.getReferralCode()).isEqualTo(UPDATED_REFERRAL_CODE);
        assertThat(testPointCustomer.getSpentMoney()).isEqualTo(UPDATED_SPENT_MONEY);
        assertThat(testPointCustomer.isActive()).isEqualTo(UPDATED_ACTIVE);
        assertThat(testPointCustomer.getDate_add()).isEqualTo(UPDATED_DATE_ADD);
    }

    @Test
    @Transactional
    public void updateNonExistingPointCustomer() throws Exception {
        int databaseSizeBeforeUpdate = pointCustomerRepository.findAll().size();

        // Create the PointCustomer
        PointCustomerDTO pointCustomerDTO = pointCustomerMapper.toDto(pointCustomer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPointCustomerMockMvc.perform(put("/api/point-customers")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pointCustomerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PointCustomer in the database
        List<PointCustomer> pointCustomerList = pointCustomerRepository.findAll();
        assertThat(pointCustomerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePointCustomer() throws Exception {
        // Initialize the database
        pointCustomerRepository.saveAndFlush(pointCustomer);

        int databaseSizeBeforeDelete = pointCustomerRepository.findAll().size();

        // Delete the pointCustomer
        restPointCustomerMockMvc.perform(delete("/api/point-customers/{id}", pointCustomer.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PointCustomer> pointCustomerList = pointCustomerRepository.findAll();
        assertThat(pointCustomerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointCustomer.class);
        PointCustomer pointCustomer1 = new PointCustomer();
        pointCustomer1.setId(1L);
        PointCustomer pointCustomer2 = new PointCustomer();
        pointCustomer2.setId(pointCustomer1.getId());
        assertThat(pointCustomer1).isEqualTo(pointCustomer2);
        pointCustomer2.setId(2L);
        assertThat(pointCustomer1).isNotEqualTo(pointCustomer2);
        pointCustomer1.setId(null);
        assertThat(pointCustomer1).isNotEqualTo(pointCustomer2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PointCustomerDTO.class);
        PointCustomerDTO pointCustomerDTO1 = new PointCustomerDTO();
        pointCustomerDTO1.setId(1L);
        PointCustomerDTO pointCustomerDTO2 = new PointCustomerDTO();
        assertThat(pointCustomerDTO1).isNotEqualTo(pointCustomerDTO2);
        pointCustomerDTO2.setId(pointCustomerDTO1.getId());
        assertThat(pointCustomerDTO1).isEqualTo(pointCustomerDTO2);
        pointCustomerDTO2.setId(2L);
        assertThat(pointCustomerDTO1).isNotEqualTo(pointCustomerDTO2);
        pointCustomerDTO1.setId(null);
        assertThat(pointCustomerDTO1).isNotEqualTo(pointCustomerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pointCustomerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pointCustomerMapper.fromId(null)).isNull();
    }
}
