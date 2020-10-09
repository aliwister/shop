package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.RewardLang;
import com.badals.shop.domain.Reward;
import com.badals.shop.repository.RewardLangRepository;
import com.badals.shop.service.RewardLangService;
import com.badals.shop.service.dto.RewardLangDTO;
import com.badals.shop.service.mapper.RewardLangMapper;
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
 * Integration tests for the {@Link RewardLangResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class RewardLangResourceIT {

    private static final String DEFAULT_LANG = "AAAAAAAAAA";
    private static final String UPDATED_LANG = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    @Autowired
    private RewardLangRepository rewardLangRepository;

    @Autowired
    private RewardLangMapper rewardLangMapper;

    @Autowired
    private RewardLangService rewardLangService;

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

    private MockMvc restRewardLangMockMvc;

    private RewardLang rewardLang;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final RewardLangResource rewardLangResource = new RewardLangResource(rewardLangService);
        this.restRewardLangMockMvc = MockMvcBuilders.standaloneSetup(rewardLangResource)
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
    public static RewardLang createEntity(EntityManager em) {
        RewardLang rewardLang = new RewardLang()
            .lang(DEFAULT_LANG)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        Reward reward;
        if (TestUtil.findAll(em, Reward.class).isEmpty()) {
            reward = RewardResourceIT.createEntity(em);
            em.persist(reward);
            em.flush();
        } else {
            reward = TestUtil.findAll(em, Reward.class).get(0);
        }
        rewardLang.setReward(reward);
        return rewardLang;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RewardLang createUpdatedEntity(EntityManager em) {
        RewardLang rewardLang = new RewardLang()
            .lang(UPDATED_LANG)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        Reward reward;
        if (TestUtil.findAll(em, Reward.class).isEmpty()) {
            reward = RewardResourceIT.createUpdatedEntity(em);
            em.persist(reward);
            em.flush();
        } else {
            reward = TestUtil.findAll(em, Reward.class).get(0);
        }
        rewardLang.setReward(reward);
        return rewardLang;
    }

    @BeforeEach
    public void initTest() {
        rewardLang = createEntity(em);
    }

    @Test
    @Transactional
    public void createRewardLang() throws Exception {
        int databaseSizeBeforeCreate = rewardLangRepository.findAll().size();

        // Create the RewardLang
        RewardLangDTO rewardLangDTO = rewardLangMapper.toDto(rewardLang);
        restRewardLangMockMvc.perform(post("/api/reward-langs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardLangDTO)))
            .andExpect(status().isCreated());

        // Validate the RewardLang in the database
        List<RewardLang> rewardLangList = rewardLangRepository.findAll();
        assertThat(rewardLangList).hasSize(databaseSizeBeforeCreate + 1);
        RewardLang testRewardLang = rewardLangList.get(rewardLangList.size() - 1);
        assertThat(testRewardLang.getLang()).isEqualTo(DEFAULT_LANG);
        assertThat(testRewardLang.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testRewardLang.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createRewardLangWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rewardLangRepository.findAll().size();

        // Create the RewardLang with an existing ID
        rewardLang.setId(1L);
        RewardLangDTO rewardLangDTO = rewardLangMapper.toDto(rewardLang);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRewardLangMockMvc.perform(post("/api/reward-langs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardLangDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RewardLang in the database
        List<RewardLang> rewardLangList = rewardLangRepository.findAll();
        assertThat(rewardLangList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLangIsRequired() throws Exception {
        int databaseSizeBeforeTest = rewardLangRepository.findAll().size();
        // set the field null
        rewardLang.setLang(null);

        // Create the RewardLang, which fails.
        RewardLangDTO rewardLangDTO = rewardLangMapper.toDto(rewardLang);

        restRewardLangMockMvc.perform(post("/api/reward-langs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardLangDTO)))
            .andExpect(status().isBadRequest());

        List<RewardLang> rewardLangList = rewardLangRepository.findAll();
        assertThat(rewardLangList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllRewardLangs() throws Exception {
        // Initialize the database
        rewardLangRepository.saveAndFlush(rewardLang);

        // Get all the rewardLangList
        restRewardLangMockMvc.perform(get("/api/reward-langs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rewardLang.getId().intValue())))
            .andExpect(jsonPath("$.[*].lang").value(hasItem(DEFAULT_LANG.toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }
    
    @Test
    @Transactional
    public void getRewardLang() throws Exception {
        // Initialize the database
        rewardLangRepository.saveAndFlush(rewardLang);

        // Get the rewardLang
        restRewardLangMockMvc.perform(get("/api/reward-langs/{id}", rewardLang.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(rewardLang.getId().intValue()))
            .andExpect(jsonPath("$.lang").value(DEFAULT_LANG.toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRewardLang() throws Exception {
        // Get the rewardLang
        restRewardLangMockMvc.perform(get("/api/reward-langs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRewardLang() throws Exception {
        // Initialize the database
        rewardLangRepository.saveAndFlush(rewardLang);

        int databaseSizeBeforeUpdate = rewardLangRepository.findAll().size();

        // Update the rewardLang
        RewardLang updatedRewardLang = rewardLangRepository.findById(rewardLang.getId()).get();
        // Disconnect from session so that the updates on updatedRewardLang are not directly saved in db
        em.detach(updatedRewardLang);
        updatedRewardLang
            .lang(UPDATED_LANG)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION);
        RewardLangDTO rewardLangDTO = rewardLangMapper.toDto(updatedRewardLang);

        restRewardLangMockMvc.perform(put("/api/reward-langs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardLangDTO)))
            .andExpect(status().isOk());

        // Validate the RewardLang in the database
        List<RewardLang> rewardLangList = rewardLangRepository.findAll();
        assertThat(rewardLangList).hasSize(databaseSizeBeforeUpdate);
        RewardLang testRewardLang = rewardLangList.get(rewardLangList.size() - 1);
        assertThat(testRewardLang.getLang()).isEqualTo(UPDATED_LANG);
        assertThat(testRewardLang.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testRewardLang.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void updateNonExistingRewardLang() throws Exception {
        int databaseSizeBeforeUpdate = rewardLangRepository.findAll().size();

        // Create the RewardLang
        RewardLangDTO rewardLangDTO = rewardLangMapper.toDto(rewardLang);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRewardLangMockMvc.perform(put("/api/reward-langs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(rewardLangDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RewardLang in the database
        List<RewardLang> rewardLangList = rewardLangRepository.findAll();
        assertThat(rewardLangList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRewardLang() throws Exception {
        // Initialize the database
        rewardLangRepository.saveAndFlush(rewardLang);

        int databaseSizeBeforeDelete = rewardLangRepository.findAll().size();

        // Delete the rewardLang
        restRewardLangMockMvc.perform(delete("/api/reward-langs/{id}", rewardLang.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RewardLang> rewardLangList = rewardLangRepository.findAll();
        assertThat(rewardLangList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardLang.class);
        RewardLang rewardLang1 = new RewardLang();
        rewardLang1.setId(1L);
        RewardLang rewardLang2 = new RewardLang();
        rewardLang2.setId(rewardLang1.getId());
        assertThat(rewardLang1).isEqualTo(rewardLang2);
        rewardLang2.setId(2L);
        assertThat(rewardLang1).isNotEqualTo(rewardLang2);
        rewardLang1.setId(null);
        assertThat(rewardLang1).isNotEqualTo(rewardLang2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(RewardLangDTO.class);
        RewardLangDTO rewardLangDTO1 = new RewardLangDTO();
        rewardLangDTO1.setId(1L);
        RewardLangDTO rewardLangDTO2 = new RewardLangDTO();
        assertThat(rewardLangDTO1).isNotEqualTo(rewardLangDTO2);
        rewardLangDTO2.setId(rewardLangDTO1.getId());
        assertThat(rewardLangDTO1).isEqualTo(rewardLangDTO2);
        rewardLangDTO2.setId(2L);
        assertThat(rewardLangDTO1).isNotEqualTo(rewardLangDTO2);
        rewardLangDTO1.setId(null);
        assertThat(rewardLangDTO1).isNotEqualTo(rewardLangDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(rewardLangMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(rewardLangMapper.fromId(null)).isNull();
    }
}
