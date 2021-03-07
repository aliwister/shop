package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.CartRule;
import com.badals.shop.repository.CartRuleRepository;
import com.badals.shop.service.CartRuleService;
import com.badals.shop.service.dto.CartRuleDTO;
import com.badals.shop.service.mapper.CartRuleMapper;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static com.badals.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link CartRuleResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class CartRuleResourceIT {

    private static final LocalDate DEFAULT_DATE_FROM = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_FROM = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATE_TO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATE_TO = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;

    private static final Integer DEFAULT_QUANTITY_PER_USER = 1;
    private static final Integer UPDATED_QUANTITY_PER_USER = 2;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;

    private static final Boolean DEFAULT_PARTIAL_USE = false;
    private static final Boolean UPDATED_PARTIAL_USE = true;

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_MINIMUM_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_MINIMUM_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_MINIMUM_AMOUNT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_MINIMUM_AMOUNT_CURRENCY = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_REDUCTION_PERCENT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REDUCTION_PERCENT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_REDUCTION_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_REDUCTION_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_REDUCTION_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_REDUCTION_CURRENCY = "BBBBBBBBBB";

    private static final Long DEFAULT_REDUCTION_PRODUCT = 1L;
    private static final Long UPDATED_REDUCTION_PRODUCT = 2L;

    private static final Boolean DEFAULT_HIGHLIGHT = false;
    private static final Boolean UPDATED_HIGHLIGHT = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    @Autowired
    private CartRuleRepository cartRuleRepository;

    @Autowired
    private CartRuleMapper cartRuleMapper;

    @Autowired
    private CartRuleService cartRuleService;

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

    private MockMvc restCartRuleMockMvc;

    private CartRule cartRule;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final CartRuleResource cartRuleResource = new CartRuleResource(cartRuleService);
        this.restCartRuleMockMvc = MockMvcBuilders.standaloneSetup(cartRuleResource)
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
    public static CartRule createEntity(EntityManager em) {
        CartRule cartRule = new CartRule()
            .dateFrom(DEFAULT_DATE_FROM)
            .dateTo(DEFAULT_DATE_TO)
            .description(DEFAULT_DESCRIPTION)
            .quantity(DEFAULT_QUANTITY)
            .quantityPerUser(DEFAULT_QUANTITY_PER_USER)
            .priority(DEFAULT_PRIORITY)
            .partialUse(DEFAULT_PARTIAL_USE)
            .code(DEFAULT_CODE)
            .minimumAmount(DEFAULT_MINIMUM_AMOUNT)
            .minimumAmountCurrency(DEFAULT_MINIMUM_AMOUNT_CURRENCY)
            .reductionPercent(DEFAULT_REDUCTION_PERCENT)
            .reductionAmount(DEFAULT_REDUCTION_AMOUNT)
            .reductionCurrency(DEFAULT_REDUCTION_CURRENCY)
            .reductionProduct(DEFAULT_REDUCTION_PRODUCT)
            .highlight(DEFAULT_HIGHLIGHT)
            .active(DEFAULT_ACTIVE);
        return cartRule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CartRule createUpdatedEntity(EntityManager em) {
        CartRule cartRule = new CartRule()
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .quantityPerUser(UPDATED_QUANTITY_PER_USER)
            .priority(UPDATED_PRIORITY)
            .partialUse(UPDATED_PARTIAL_USE)
            .code(UPDATED_CODE)
            .minimumAmount(UPDATED_MINIMUM_AMOUNT)
            .minimumAmountCurrency(UPDATED_MINIMUM_AMOUNT_CURRENCY)
            .reductionPercent(UPDATED_REDUCTION_PERCENT)
            .reductionAmount(UPDATED_REDUCTION_AMOUNT)
            .reductionCurrency(UPDATED_REDUCTION_CURRENCY)
            .reductionProduct(UPDATED_REDUCTION_PRODUCT)
            .highlight(UPDATED_HIGHLIGHT)
            .active(UPDATED_ACTIVE);
        return cartRule;
    }

    @BeforeEach
    public void initTest() {
        cartRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createCartRule() throws Exception {
        int databaseSizeBeforeCreate = cartRuleRepository.findAll().size();

        // Create the CartRule
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);
        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isCreated());

        // Validate the CartRule in the database
        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeCreate + 1);
        CartRule testCartRule = cartRuleList.get(cartRuleList.size() - 1);
        assertThat(testCartRule.getDateFrom()).isEqualTo(DEFAULT_DATE_FROM);
        assertThat(testCartRule.getDateTo()).isEqualTo(DEFAULT_DATE_TO);
        assertThat(testCartRule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testCartRule.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testCartRule.getQuantityPerUser()).isEqualTo(DEFAULT_QUANTITY_PER_USER);
        assertThat(testCartRule.getPriority()).isEqualTo(DEFAULT_PRIORITY);
        assertThat(testCartRule.isPartialUse()).isEqualTo(DEFAULT_PARTIAL_USE);
        assertThat(testCartRule.getCode()).isEqualTo(DEFAULT_CODE);
        assertThat(testCartRule.getMinimumAmount()).isEqualTo(DEFAULT_MINIMUM_AMOUNT);
        assertThat(testCartRule.getMinimumAmountCurrency()).isEqualTo(DEFAULT_MINIMUM_AMOUNT_CURRENCY);
        assertThat(testCartRule.getReductionPercent()).isEqualTo(DEFAULT_REDUCTION_PERCENT);
        assertThat(testCartRule.getReductionAmount()).isEqualTo(DEFAULT_REDUCTION_AMOUNT);
        assertThat(testCartRule.getReductionCurrency()).isEqualTo(DEFAULT_REDUCTION_CURRENCY);
        assertThat(testCartRule.getReductionProduct()).isEqualTo(DEFAULT_REDUCTION_PRODUCT);
        assertThat(testCartRule.isHighlight()).isEqualTo(DEFAULT_HIGHLIGHT);
        assertThat(testCartRule.isActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    public void createCartRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = cartRuleRepository.findAll().size();

        // Create the CartRule with an existing ID
        cartRule.setId(1L);
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartRule in the database
        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkDateFromIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setDateFrom(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkDateToIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setDateTo(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setQuantity(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkQuantityPerUserIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setQuantityPerUser(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPartialUseIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setPartialUse(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setCode(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkReductionCurrencyIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setReductionCurrency(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIsRequired() throws Exception {
        int databaseSizeBeforeTest = cartRuleRepository.findAll().size();
        // set the field null
        cartRule.setActive(null);

        // Create the CartRule, which fails.
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        restCartRuleMockMvc.perform(post("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCartRules() throws Exception {
        // Initialize the database
        cartRuleRepository.saveAndFlush(cartRule);

        // Get all the cartRuleList
        restCartRuleMockMvc.perform(get("/api/cart-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cartRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].dateFrom").value(hasItem(DEFAULT_DATE_FROM.toString())))
            .andExpect(jsonPath("$.[*].dateTo").value(hasItem(DEFAULT_DATE_TO.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].quantityPerUser").value(hasItem(DEFAULT_QUANTITY_PER_USER)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].partialUse").value(hasItem(DEFAULT_PARTIAL_USE.booleanValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE.toString())))
            .andExpect(jsonPath("$.[*].minimumAmount").value(hasItem(DEFAULT_MINIMUM_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].minimumAmountCurrency").value(hasItem(DEFAULT_MINIMUM_AMOUNT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].reductionPercent").value(hasItem(DEFAULT_REDUCTION_PERCENT.intValue())))
            .andExpect(jsonPath("$.[*].reductionAmount").value(hasItem(DEFAULT_REDUCTION_AMOUNT.intValue())))
            .andExpect(jsonPath("$.[*].reductionCurrency").value(hasItem(DEFAULT_REDUCTION_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].reductionProduct").value(hasItem(DEFAULT_REDUCTION_PRODUCT.intValue())))
            .andExpect(jsonPath("$.[*].highlight").value(hasItem(DEFAULT_HIGHLIGHT.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getCartRule() throws Exception {
        // Initialize the database
        cartRuleRepository.saveAndFlush(cartRule);

        // Get the cartRule
        restCartRuleMockMvc.perform(get("/api/cart-rules/{id}", cartRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cartRule.getId().intValue()))
            .andExpect(jsonPath("$.dateFrom").value(DEFAULT_DATE_FROM.toString()))
            .andExpect(jsonPath("$.dateTo").value(DEFAULT_DATE_TO.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.quantityPerUser").value(DEFAULT_QUANTITY_PER_USER))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.partialUse").value(DEFAULT_PARTIAL_USE.booleanValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE.toString()))
            .andExpect(jsonPath("$.minimumAmount").value(DEFAULT_MINIMUM_AMOUNT.intValue()))
            .andExpect(jsonPath("$.minimumAmountCurrency").value(DEFAULT_MINIMUM_AMOUNT_CURRENCY.toString()))
            .andExpect(jsonPath("$.reductionPercent").value(DEFAULT_REDUCTION_PERCENT.intValue()))
            .andExpect(jsonPath("$.reductionAmount").value(DEFAULT_REDUCTION_AMOUNT.intValue()))
            .andExpect(jsonPath("$.reductionCurrency").value(DEFAULT_REDUCTION_CURRENCY.toString()))
            .andExpect(jsonPath("$.reductionProduct").value(DEFAULT_REDUCTION_PRODUCT.intValue()))
            .andExpect(jsonPath("$.highlight").value(DEFAULT_HIGHLIGHT.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingCartRule() throws Exception {
        // Get the cartRule
        restCartRuleMockMvc.perform(get("/api/cart-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCartRule() throws Exception {
        // Initialize the database
        cartRuleRepository.saveAndFlush(cartRule);

        int databaseSizeBeforeUpdate = cartRuleRepository.findAll().size();

        // Update the cartRule
        CartRule updatedCartRule = cartRuleRepository.findById(cartRule.getId()).get();
        // Disconnect from session so that the updates on updatedCartRule are not directly saved in db
        em.detach(updatedCartRule);
        updatedCartRule
            .dateFrom(UPDATED_DATE_FROM)
            .dateTo(UPDATED_DATE_TO)
            .description(UPDATED_DESCRIPTION)
            .quantity(UPDATED_QUANTITY)
            .quantityPerUser(UPDATED_QUANTITY_PER_USER)
            .priority(UPDATED_PRIORITY)
            .partialUse(UPDATED_PARTIAL_USE)
            .code(UPDATED_CODE)
            .minimumAmount(UPDATED_MINIMUM_AMOUNT)
            .minimumAmountCurrency(UPDATED_MINIMUM_AMOUNT_CURRENCY)
            .reductionPercent(UPDATED_REDUCTION_PERCENT)
            .reductionAmount(UPDATED_REDUCTION_AMOUNT)
            .reductionCurrency(UPDATED_REDUCTION_CURRENCY)
            .reductionProduct(UPDATED_REDUCTION_PRODUCT)
            .highlight(UPDATED_HIGHLIGHT)
            .active(UPDATED_ACTIVE);
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(updatedCartRule);

        restCartRuleMockMvc.perform(put("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isOk());

        // Validate the CartRule in the database
        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeUpdate);
        CartRule testCartRule = cartRuleList.get(cartRuleList.size() - 1);
        assertThat(testCartRule.getDateFrom()).isEqualTo(UPDATED_DATE_FROM);
        assertThat(testCartRule.getDateTo()).isEqualTo(UPDATED_DATE_TO);
        assertThat(testCartRule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testCartRule.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testCartRule.getQuantityPerUser()).isEqualTo(UPDATED_QUANTITY_PER_USER);
        assertThat(testCartRule.getPriority()).isEqualTo(UPDATED_PRIORITY);
        assertThat(testCartRule.isPartialUse()).isEqualTo(UPDATED_PARTIAL_USE);
        assertThat(testCartRule.getCode()).isEqualTo(UPDATED_CODE);
        assertThat(testCartRule.getMinimumAmount()).isEqualTo(UPDATED_MINIMUM_AMOUNT);
        assertThat(testCartRule.getMinimumAmountCurrency()).isEqualTo(UPDATED_MINIMUM_AMOUNT_CURRENCY);
        assertThat(testCartRule.getReductionPercent()).isEqualTo(UPDATED_REDUCTION_PERCENT);
        assertThat(testCartRule.getReductionAmount()).isEqualTo(UPDATED_REDUCTION_AMOUNT);
        assertThat(testCartRule.getReductionCurrency()).isEqualTo(UPDATED_REDUCTION_CURRENCY);
        assertThat(testCartRule.getReductionProduct()).isEqualTo(UPDATED_REDUCTION_PRODUCT);
        assertThat(testCartRule.isHighlight()).isEqualTo(UPDATED_HIGHLIGHT);
        assertThat(testCartRule.isActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    public void updateNonExistingCartRule() throws Exception {
        int databaseSizeBeforeUpdate = cartRuleRepository.findAll().size();

        // Create the CartRule
        CartRuleDTO cartRuleDTO = cartRuleMapper.toDto(cartRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCartRuleMockMvc.perform(put("/api/cart-rules")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(cartRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CartRule in the database
        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCartRule() throws Exception {
        // Initialize the database
        cartRuleRepository.saveAndFlush(cartRule);

        int databaseSizeBeforeDelete = cartRuleRepository.findAll().size();

        // Delete the cartRule
        restCartRuleMockMvc.perform(delete("/api/cart-rules/{id}", cartRule.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CartRule> cartRuleList = cartRuleRepository.findAll();
        assertThat(cartRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartRule.class);
        CartRule cartRule1 = new CartRule();
        cartRule1.setId(1L);
        CartRule cartRule2 = new CartRule();
        cartRule2.setId(cartRule1.getId());
        assertThat(cartRule1).isEqualTo(cartRule2);
        cartRule2.setId(2L);
        assertThat(cartRule1).isNotEqualTo(cartRule2);
        cartRule1.setId(null);
        assertThat(cartRule1).isNotEqualTo(cartRule2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CartRuleDTO.class);
        CartRuleDTO cartRuleDTO1 = new CartRuleDTO();
        cartRuleDTO1.setId(1L);
        CartRuleDTO cartRuleDTO2 = new CartRuleDTO();
        assertThat(cartRuleDTO1).isNotEqualTo(cartRuleDTO2);
        cartRuleDTO2.setId(cartRuleDTO1.getId());
        assertThat(cartRuleDTO1).isEqualTo(cartRuleDTO2);
        cartRuleDTO2.setId(2L);
        assertThat(cartRuleDTO1).isNotEqualTo(cartRuleDTO2);
        cartRuleDTO1.setId(null);
        assertThat(cartRuleDTO1).isNotEqualTo(cartRuleDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(cartRuleMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(cartRuleMapper.fromId(null)).isNull();
    }
}
