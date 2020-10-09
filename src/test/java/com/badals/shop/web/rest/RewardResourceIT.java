package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.Reward;
import com.badals.shop.repository.RewardRepository;
import com.badals.shop.service.RewardService;
import com.badals.shop.service.dto.RewardDTO;
import com.badals.shop.service.mapper.RewardMapper;
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

import com.badals.shop.domain.enumeration.DiscountReductionType;
/**
 * Integration tests for the {@Link RewardResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class RewardResourceIT {

    private static final String DEFAULT_REWARD_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_REWARD_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_POINTS = 1L;
    private static final Long UPDATED_POINTS = 2L;

    private static final Long DEFAULT_RADIUS = 1L;
    private static final Long UPDATED_RADIUS = 2L;

    private static final Long DEFAULT_MINIMUM_CART_AMOUNT = 1L;
    private static final Long UPDATED_MINIMUM_CART_AMOUNT = 2L;

    private static final Long DEFAULT_DISCOUNT_VALUE = 1L;
    private static final Long UPDATED_DISCOUNT_VALUE = 2L;

    private static final Long DEFAULT_DISCOUNT_VALID_DAYS = 1L;
    private static final Long UPDATED_DISCOUNT_VALID_DAYS = 2L;

    private static final DiscountReductionType DEFAULT_DISCOUNT_REDUCTION_TYPE = DiscountReductionType.AMOUNT;
    private static final DiscountReductionType UPDATED_DISCOUNT_REDUCTION_TYPE = DiscountReductionType.PERCENT;

    @Autowired
    private RewardRepository rewardRepository;

    @Autowired
    private RewardMapper rewardMapper;

    @Autowired
    private RewardService rewardService;

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

    private MockMvc restRewardMockMvc;

    private Reward reward;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RewardResource rewardResource = new RewardResource(rewardService);
        this.restRewardMockMvc = MockMvcBuilders.standaloneSetup(rewardResource)
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
    public static Reward createEntity(EntityManager em) {
        Reward reward = new Reward()
            .rewardType(DEFAULT_REWARD_TYPE)
            .points(DEFAULT_POINTS)
            .radius(DEFAULT_RADIUS)
            .minimumCartAmount(DEFAULT_MINIMUM_CART_AMOUNT)
            .discountValue(DEFAULT_DISCOUNT_VALUE)
            .discountValidDays(DEFAULT_DISCOUNT_VALID_DAYS)
            .discountReductionType(DEFAULT_DISCOUNT_REDUCTION_TYPE);
        return reward;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reward createUpdatedEntity(EntityManager em) {
        Reward reward = new Reward()
            .rewardType(UPDATED_REWARD_TYPE)
            .points(UPDATED_POINTS)
            .radius(UPDATED_RADIUS)
            .minimumCartAmount(UPDATED_MINIMUM_CART_AMOUNT)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .discountValidDays(UPDATED_DISCOUNT_VALID_DAYS)
            .discountReductionType(UPDATED_DISCOUNT_REDUCTION_TYPE);
        return reward;
    }

    @BeforeEach
    public void initTest() {
        reward = createEntity(em);
    }

    @Test
    @Transactional
    public void createReward() throws Exception {
        int databaseSizeBeforeCreate = rewardRepository.findAll().size();

        // Create the Reward
        RewardDTO rewardDTO = rewardMapper.toDto(reward);
        restRewardMockMvc.perform(post("/api/rewards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardDTO)))
            .andExpect(status().isCreated());

        // Validate the Reward in the database
        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeCreate + 1);
        Reward testReward = rewardList.get(rewardList.size() - 1);
        assertThat(testReward.getRewardType()).isEqualTo(DEFAULT_REWARD_TYPE);
        assertThat(testReward.getPoints()).isEqualTo(DEFAULT_POINTS);
        assertThat(testReward.getRadius()).isEqualTo(DEFAULT_RADIUS);
        assertThat(testReward.getMinimumCartAmount()).isEqualTo(DEFAULT_MINIMUM_CART_AMOUNT);
        assertThat(testReward.getDiscountValue()).isEqualTo(DEFAULT_DISCOUNT_VALUE);
        assertThat(testReward.getDiscountValidDays()).isEqualTo(DEFAULT_DISCOUNT_VALID_DAYS);
        assertThat(testReward.getDiscountReductionType()).isEqualTo(DEFAULT_DISCOUNT_REDUCTION_TYPE);
    }

    @Test
    @Transactional
    public void createRewardWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rewardRepository.findAll().size();

        // Create the Reward with an existing ID
        reward.setId(1L);
        RewardDTO rewardDTO = rewardMapper.toDto(reward);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRewardMockMvc.perform(post("/api/rewards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPointsIsRequired() throws Exception {
        int databaseSizeBeforeTest = rewardRepository.findAll().size();
        // set the field null
        reward.setPoints(null);

        // Create the Reward, which fails.
        RewardDTO rewardDTO = rewardMapper.toDto(reward);

        restRewardMockMvc.perform(post("/api/rewards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardDTO)))
            .andExpect(status().isBadRequest());

        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDiscountReductionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = rewardRepository.findAll().size();
        // set the field null
        reward.setDiscountReductionType(null);

        // Create the Reward, which fails.
        RewardDTO rewardDTO = rewardMapper.toDto(reward);

        restRewardMockMvc.perform(post("/api/rewards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardDTO)))
            .andExpect(status().isBadRequest());

        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRewards() throws Exception {
        // Initialize the database
        rewardRepository.saveAndFlush(reward);

        // Get all the rewardList
        restRewardMockMvc.perform(get("/api/rewards?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reward.getId().intValue())))
            .andExpect(jsonPath("$.[*].rewardType").value(hasItem(DEFAULT_REWARD_TYPE.toString())))
            .andExpect(jsonPath("$.[*].points").value(hasItem(DEFAULT_POINTS.intValue())))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.intValue())))
            .andExpect(jsonPath("$.[*].minimumCartAmount").value(hasItem(DEFAULT_MINIMUM_CART_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].discountValue").value(hasItem(DEFAULT_DISCOUNT_VALUE.intValue())))
            .andExpect(jsonPath("$.[*].discountValidDays").value(hasItem(DEFAULT_DISCOUNT_VALID_DAYS.intValue())))
            .andExpect(jsonPath("$.[*].discountReductionType").value(hasItem(DEFAULT_DISCOUNT_REDUCTION_TYPE.toString())));
    }
    
    @Test
    @Transactional
    public void getReward() throws Exception {
        // Initialize the database
        rewardRepository.saveAndFlush(reward);

        // Get the reward
        restRewardMockMvc.perform(get("/api/rewards/{id}", reward.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(reward.getId().intValue()))
            .andExpect(jsonPath("$.rewardType").value(DEFAULT_REWARD_TYPE.toString()))
            .andExpect(jsonPath("$.points").value(DEFAULT_POINTS.intValue()))
            .andExpect(jsonPath("$.radius").value(DEFAULT_RADIUS.intValue()))
            .andExpect(jsonPath("$.minimumCartAmount").value(DEFAULT_MINIMUM_CART_AMOUNT.intValue()))
            .andExpect(jsonPath("$.discountValue").value(DEFAULT_DISCOUNT_VALUE.intValue()))
            .andExpect(jsonPath("$.discountValidDays").value(DEFAULT_DISCOUNT_VALID_DAYS.intValue()))
            .andExpect(jsonPath("$.discountReductionType").value(DEFAULT_DISCOUNT_REDUCTION_TYPE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingReward() throws Exception {
        // Get the reward
        restRewardMockMvc.perform(get("/api/rewards/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReward() throws Exception {
        // Initialize the database
        rewardRepository.saveAndFlush(reward);

        int databaseSizeBeforeUpdate = rewardRepository.findAll().size();

        // Update the reward
        Reward updatedReward = rewardRepository.findById(reward.getId()).get();
        // Disconnect from session so that the updates on updatedReward are not directly saved in db
        em.detach(updatedReward);
        updatedReward
            .rewardType(UPDATED_REWARD_TYPE)
            .points(UPDATED_POINTS)
            .radius(UPDATED_RADIUS)
            .minimumCartAmount(UPDATED_MINIMUM_CART_AMOUNT)
            .discountValue(UPDATED_DISCOUNT_VALUE)
            .discountValidDays(UPDATED_DISCOUNT_VALID_DAYS)
            .discountReductionType(UPDATED_DISCOUNT_REDUCTION_TYPE);
        RewardDTO rewardDTO = rewardMapper.toDto(updatedReward);

        restRewardMockMvc.perform(put("/api/rewards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardDTO)))
            .andExpect(status().isOk());

        // Validate the Reward in the database
        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeUpdate);
        Reward testReward = rewardList.get(rewardList.size() - 1);
        assertThat(testReward.getRewardType()).isEqualTo(UPDATED_REWARD_TYPE);
        assertThat(testReward.getPoints()).isEqualTo(UPDATED_POINTS);
        assertThat(testReward.getRadius()).isEqualTo(UPDATED_RADIUS);
        assertThat(testReward.getMinimumCartAmount()).isEqualTo(UPDATED_MINIMUM_CART_AMOUNT);
        assertThat(testReward.getDiscountValue()).isEqualTo(UPDATED_DISCOUNT_VALUE);
        assertThat(testReward.getDiscountValidDays()).isEqualTo(UPDATED_DISCOUNT_VALID_DAYS);
        assertThat(testReward.getDiscountReductionType()).isEqualTo(UPDATED_DISCOUNT_REDUCTION_TYPE);
    }

    @Test
    @Transactional
    public void updateNonExistingReward() throws Exception {
        int databaseSizeBeforeUpdate = rewardRepository.findAll().size();

        // Create the Reward
        RewardDTO rewardDTO = rewardMapper.toDto(reward);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardMockMvc.perform(put("/api/rewards")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reward in the database
        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReward() throws Exception {
        // Initialize the database
        rewardRepository.saveAndFlush(reward);

        int databaseSizeBeforeDelete = rewardRepository.findAll().size();

        // Delete the reward
        restRewardMockMvc.perform(delete("/api/rewards/{id}", reward.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reward> rewardList = rewardRepository.findAll();
        assertThat(rewardList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reward.class);
        Reward reward1 = new Reward();
        reward1.setId(1L);
        Reward reward2 = new Reward();
        reward2.setId(reward1.getId());
        assertThat(reward1).isEqualTo(reward2);
        reward2.setId(2L);
        assertThat(reward1).isNotEqualTo(reward2);
        reward1.setId(null);
        assertThat(reward1).isNotEqualTo(reward2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardDTO.class);
        RewardDTO rewardDTO1 = new RewardDTO();
        rewardDTO1.setId(1L);
        RewardDTO rewardDTO2 = new RewardDTO();
        assertThat(rewardDTO1).isNotEqualTo(rewardDTO2);
        rewardDTO2.setId(rewardDTO1.getId());
        assertThat(rewardDTO1).isEqualTo(rewardDTO2);
        rewardDTO2.setId(2L);
        assertThat(rewardDTO1).isNotEqualTo(rewardDTO2);
        rewardDTO1.setId(null);
        assertThat(rewardDTO1).isNotEqualTo(rewardDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rewardMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rewardMapper.fromId(null)).isNull();
    }
}
