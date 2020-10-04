package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.Hashtag;
import com.badals.shop.repository.HashtagRepository;
import com.badals.shop.service.HashtagService;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.mapper.HashtagMapper;
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
 * Integration tests for the {@Link HashtagResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class HashtagResourceIT {

    private static final String DEFAULT_EN = "AAAAAAAAAA";
    private static final String UPDATED_EN = "BBBBBBBBBB";

    private static final String DEFAULT_AR = "AAAAAAAAAA";
    private static final String UPDATED_AR = "BBBBBBBBBB";

    @Autowired
    private HashtagRepository hashtagRepository;

    @Autowired
    private HashtagMapper hashtagMapper;

    @Autowired
    private HashtagService hashtagService;

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

    private MockMvc restHashtagMockMvc;

    private Hashtag hashtag;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final HashtagResource hashtagResource = new HashtagResource(hashtagService);
        this.restHashtagMockMvc = MockMvcBuilders.standaloneSetup(hashtagResource)
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
    public static Hashtag createEntity(EntityManager em) {
        Hashtag hashtag = new Hashtag()
            .en(DEFAULT_EN)
            .ar(DEFAULT_AR);
        return hashtag;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Hashtag createUpdatedEntity(EntityManager em) {
        Hashtag hashtag = new Hashtag()
            .en(UPDATED_EN)
            .ar(UPDATED_AR);
        return hashtag;
    }

    @BeforeEach
    public void initTest() {
        hashtag = createEntity(em);
    }

    @Test
    @Transactional
    public void createHashtag() throws Exception {
        int databaseSizeBeforeCreate = hashtagRepository.findAll().size();

        // Create the Hashtag
        HashtagDTO hashtagDTO = hashtagMapper.toDto(hashtag);
        restHashtagMockMvc.perform(post("/api/hashtags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hashtagDTO)))
            .andExpect(status().isCreated());

        // Validate the Hashtag in the database
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        assertThat(hashtagList).hasSize(databaseSizeBeforeCreate + 1);
        Hashtag testHashtag = hashtagList.get(hashtagList.size() - 1);
        assertThat(testHashtag.getEn()).isEqualTo(DEFAULT_EN);
        assertThat(testHashtag.getAr()).isEqualTo(DEFAULT_AR);
    }

    @Test
    @Transactional
    public void createHashtagWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = hashtagRepository.findAll().size();

        // Create the Hashtag with an existing ID
        hashtag.setId(1L);
        HashtagDTO hashtagDTO = hashtagMapper.toDto(hashtag);

        // An entity with an existing ID cannot be created, so this API call must fail
        restHashtagMockMvc.perform(post("/api/hashtags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hashtagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hashtag in the database
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        assertThat(hashtagList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllHashtags() throws Exception {
        // Initialize the database
        hashtagRepository.saveAndFlush(hashtag);

        // Get all the hashtagList
        restHashtagMockMvc.perform(get("/api/hashtags?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hashtag.getId().intValue())))
            .andExpect(jsonPath("$.[*].en").value(hasItem(DEFAULT_EN.toString())))
            .andExpect(jsonPath("$.[*].ar").value(hasItem(DEFAULT_AR.toString())));
    }
    
    @Test
    @Transactional
    public void getHashtag() throws Exception {
        // Initialize the database
        hashtagRepository.saveAndFlush(hashtag);

        // Get the hashtag
        restHashtagMockMvc.perform(get("/api/hashtags/{id}", hashtag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(hashtag.getId().intValue()))
            .andExpect(jsonPath("$.en").value(DEFAULT_EN.toString()))
            .andExpect(jsonPath("$.ar").value(DEFAULT_AR.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingHashtag() throws Exception {
        // Get the hashtag
        restHashtagMockMvc.perform(get("/api/hashtags/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateHashtag() throws Exception {
        // Initialize the database
        hashtagRepository.saveAndFlush(hashtag);

        int databaseSizeBeforeUpdate = hashtagRepository.findAll().size();

        // Update the hashtag
        Hashtag updatedHashtag = hashtagRepository.findById(hashtag.getId()).get();
        // Disconnect from session so that the updates on updatedHashtag are not directly saved in db
        em.detach(updatedHashtag);
        updatedHashtag
            .en(UPDATED_EN)
            .ar(UPDATED_AR);
        HashtagDTO hashtagDTO = hashtagMapper.toDto(updatedHashtag);

        restHashtagMockMvc.perform(put("/api/hashtags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hashtagDTO)))
            .andExpect(status().isOk());

        // Validate the Hashtag in the database
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        assertThat(hashtagList).hasSize(databaseSizeBeforeUpdate);
        Hashtag testHashtag = hashtagList.get(hashtagList.size() - 1);
        assertThat(testHashtag.getEn()).isEqualTo(UPDATED_EN);
        assertThat(testHashtag.getAr()).isEqualTo(UPDATED_AR);
    }

    @Test
    @Transactional
    public void updateNonExistingHashtag() throws Exception {
        int databaseSizeBeforeUpdate = hashtagRepository.findAll().size();

        // Create the Hashtag
        HashtagDTO hashtagDTO = hashtagMapper.toDto(hashtag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHashtagMockMvc.perform(put("/api/hashtags")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(hashtagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Hashtag in the database
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        assertThat(hashtagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteHashtag() throws Exception {
        // Initialize the database
        hashtagRepository.saveAndFlush(hashtag);

        int databaseSizeBeforeDelete = hashtagRepository.findAll().size();

        // Delete the hashtag
        restHashtagMockMvc.perform(delete("/api/hashtags/{id}", hashtag.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Hashtag> hashtagList = hashtagRepository.findAll();
        assertThat(hashtagList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Hashtag.class);
        Hashtag hashtag1 = new Hashtag();
        hashtag1.setId(1L);
        Hashtag hashtag2 = new Hashtag();
        hashtag2.setId(hashtag1.getId());
        assertThat(hashtag1).isEqualTo(hashtag2);
        hashtag2.setId(2L);
        assertThat(hashtag1).isNotEqualTo(hashtag2);
        hashtag1.setId(null);
        assertThat(hashtag1).isNotEqualTo(hashtag2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(HashtagDTO.class);
        HashtagDTO hashtagDTO1 = new HashtagDTO();
        hashtagDTO1.setId(1L);
        HashtagDTO hashtagDTO2 = new HashtagDTO();
        assertThat(hashtagDTO1).isNotEqualTo(hashtagDTO2);
        hashtagDTO2.setId(hashtagDTO1.getId());
        assertThat(hashtagDTO1).isEqualTo(hashtagDTO2);
        hashtagDTO2.setId(2L);
        assertThat(hashtagDTO1).isNotEqualTo(hashtagDTO2);
        hashtagDTO1.setId(null);
        assertThat(hashtagDTO1).isNotEqualTo(hashtagDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(hashtagMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(hashtagMapper.fromId(null)).isNull();
    }
}
