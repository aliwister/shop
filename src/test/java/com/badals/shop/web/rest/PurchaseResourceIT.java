package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.Purchase;
import com.badals.shop.repository.PurchaseRepository;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.service.mapper.PurchaseMapper;
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
 * Integration tests for the {@Link PurchaseResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class PurchaseResourceIT {

    private static final Long DEFAULT_PO = 1L;
    private static final Long UPDATED_PO = 2L;

    private static final String DEFAULT_REF = "AAAAAAAAAA";
    private static final String UPDATED_REF = "BBBBBBBBBB";

    private static final String DEFAULT_SHIPPING_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_CURRENCY = "AAAAAAAAAA";
    private static final String UPDATED_CURRENCY = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_INVOICE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_INVOICE_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ESTIMATED_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_ORDER_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ORDER_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final BigDecimal DEFAULT_SUBTOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_SUBTOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DELIVERY_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_DELIVERY_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TAXES_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TAXES_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_DISCOUNT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_DISCOUNT_TOTAL = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TOTAL = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL = new BigDecimal(2);

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private PurchaseMapper purchaseMapper;

    @Autowired
    private PurchaseService purchaseService;

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

    private MockMvc restPurchaseMockMvc;

    private Purchase purchase;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseResource purchaseResource = new PurchaseResource(purchaseService);
        this.restPurchaseMockMvc = MockMvcBuilders.standaloneSetup(purchaseResource)
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
    public static Purchase createEntity(EntityManager em) {
        Purchase purchase = new Purchase()
            .po(DEFAULT_PO)
            .ref(DEFAULT_REF)
            .shippingInstructions(DEFAULT_SHIPPING_INSTRUCTIONS)
            .currency(DEFAULT_CURRENCY)
            .invoiceDate(DEFAULT_INVOICE_DATE)
            .estimatedDeliveryDate(DEFAULT_ESTIMATED_DELIVERY_DATE)
            .orderDate(DEFAULT_ORDER_DATE)
            .subtotal(DEFAULT_SUBTOTAL)
            .deliveryTotal(DEFAULT_DELIVERY_TOTAL)
            .taxesTotal(DEFAULT_TAXES_TOTAL)
            .discountTotal(DEFAULT_DISCOUNT_TOTAL)
            .total(DEFAULT_TOTAL);
        return purchase;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Purchase createUpdatedEntity(EntityManager em) {
        Purchase purchase = new Purchase()
            .po(UPDATED_PO)
            .ref(UPDATED_REF)
            .shippingInstructions(UPDATED_SHIPPING_INSTRUCTIONS)
            .currency(UPDATED_CURRENCY)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .orderDate(UPDATED_ORDER_DATE)
            .subtotal(UPDATED_SUBTOTAL)
            .deliveryTotal(UPDATED_DELIVERY_TOTAL)
            .taxesTotal(UPDATED_TAXES_TOTAL)
            .discountTotal(UPDATED_DISCOUNT_TOTAL)
            .total(UPDATED_TOTAL);
        return purchase;
    }

    @BeforeEach
    public void initTest() {
        purchase = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchase() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);
        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isCreated());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeCreate + 1);
        Purchase testPurchase = purchaseList.get(purchaseList.size() - 1);
        assertThat(testPurchase.getPo()).isEqualTo(DEFAULT_PO);
        assertThat(testPurchase.getRef()).isEqualTo(DEFAULT_REF);
        assertThat(testPurchase.getShippingInstructions()).isEqualTo(DEFAULT_SHIPPING_INSTRUCTIONS);
        assertThat(testPurchase.getCurrency()).isEqualTo(DEFAULT_CURRENCY);
        assertThat(testPurchase.getInvoiceDate()).isEqualTo(DEFAULT_INVOICE_DATE);
        assertThat(testPurchase.getEstimatedDeliveryDate()).isEqualTo(DEFAULT_ESTIMATED_DELIVERY_DATE);
        assertThat(testPurchase.getOrderDate()).isEqualTo(DEFAULT_ORDER_DATE);
        assertThat(testPurchase.getSubtotal()).isEqualTo(DEFAULT_SUBTOTAL);
        assertThat(testPurchase.getDeliveryTotal()).isEqualTo(DEFAULT_DELIVERY_TOTAL);
        assertThat(testPurchase.getTaxesTotal()).isEqualTo(DEFAULT_TAXES_TOTAL);
        assertThat(testPurchase.getDiscountTotal()).isEqualTo(DEFAULT_DISCOUNT_TOTAL);
        assertThat(testPurchase.getTotal()).isEqualTo(DEFAULT_TOTAL);
    }

    @Test
    @Transactional
    public void createPurchaseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseRepository.findAll().size();

        // Create the Purchase with an existing ID
        purchase.setId(1L);
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPoIsRequired() throws Exception {
        int databaseSizeBeforeTest = purchaseRepository.findAll().size();
        // set the field null
        purchase.setPo(null);

        // Create the Purchase, which fails.
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        restPurchaseMockMvc.perform(post("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPurchases() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get all the purchaseList
        restPurchaseMockMvc.perform(get("/api/purchases?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchase.getId().intValue())))
            .andExpect(jsonPath("$.[*].po").value(hasItem(DEFAULT_PO.intValue())))
            .andExpect(jsonPath("$.[*].ref").value(hasItem(DEFAULT_REF.toString())))
            .andExpect(jsonPath("$.[*].shippingInstructions").value(hasItem(DEFAULT_SHIPPING_INSTRUCTIONS.toString())))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].invoiceDate").value(hasItem(DEFAULT_INVOICE_DATE.toString())))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].subtotal").value(hasItem(DEFAULT_SUBTOTAL.intValue())))
            .andExpect(jsonPath("$.[*].deliveryTotal").value(hasItem(DEFAULT_DELIVERY_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].taxesTotal").value(hasItem(DEFAULT_TAXES_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].discountTotal").value(hasItem(DEFAULT_DISCOUNT_TOTAL.intValue())))
            .andExpect(jsonPath("$.[*].total").value(hasItem(DEFAULT_TOTAL.intValue())));
    }
    
    @Test
    @Transactional
    public void getPurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", purchase.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchase.getId().intValue()))
            .andExpect(jsonPath("$.po").value(DEFAULT_PO.intValue()))
            .andExpect(jsonPath("$.ref").value(DEFAULT_REF.toString()))
            .andExpect(jsonPath("$.shippingInstructions").value(DEFAULT_SHIPPING_INSTRUCTIONS.toString()))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.invoiceDate").value(DEFAULT_INVOICE_DATE.toString()))
            .andExpect(jsonPath("$.estimatedDeliveryDate").value(DEFAULT_ESTIMATED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.subtotal").value(DEFAULT_SUBTOTAL.intValue()))
            .andExpect(jsonPath("$.deliveryTotal").value(DEFAULT_DELIVERY_TOTAL.intValue()))
            .andExpect(jsonPath("$.taxesTotal").value(DEFAULT_TAXES_TOTAL.intValue()))
            .andExpect(jsonPath("$.discountTotal").value(DEFAULT_DISCOUNT_TOTAL.intValue()))
            .andExpect(jsonPath("$.total").value(DEFAULT_TOTAL.intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingPurchase() throws Exception {
        // Get the purchase
        restPurchaseMockMvc.perform(get("/api/purchases/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Update the purchase
        Purchase updatedPurchase = purchaseRepository.findById(purchase.getId()).get();
        // Disconnect from session so that the updates on updatedPurchase are not directly saved in db
        em.detach(updatedPurchase);
        updatedPurchase
            .po(UPDATED_PO)
            .ref(UPDATED_REF)
            .shippingInstructions(UPDATED_SHIPPING_INSTRUCTIONS)
            .currency(UPDATED_CURRENCY)
            .invoiceDate(UPDATED_INVOICE_DATE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .orderDate(UPDATED_ORDER_DATE)
            .subtotal(UPDATED_SUBTOTAL)
            .deliveryTotal(UPDATED_DELIVERY_TOTAL)
            .taxesTotal(UPDATED_TAXES_TOTAL)
            .discountTotal(UPDATED_DISCOUNT_TOTAL)
            .total(UPDATED_TOTAL);
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(updatedPurchase);

        restPurchaseMockMvc.perform(put("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isOk());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeUpdate);
        Purchase testPurchase = purchaseList.get(purchaseList.size() - 1);
        assertThat(testPurchase.getPo()).isEqualTo(UPDATED_PO);
        assertThat(testPurchase.getRef()).isEqualTo(UPDATED_REF);
        assertThat(testPurchase.getShippingInstructions()).isEqualTo(UPDATED_SHIPPING_INSTRUCTIONS);
        assertThat(testPurchase.getCurrency()).isEqualTo(UPDATED_CURRENCY);
        assertThat(testPurchase.getInvoiceDate()).isEqualTo(UPDATED_INVOICE_DATE);
        assertThat(testPurchase.getEstimatedDeliveryDate()).isEqualTo(UPDATED_ESTIMATED_DELIVERY_DATE);
        assertThat(testPurchase.getOrderDate()).isEqualTo(UPDATED_ORDER_DATE);
        assertThat(testPurchase.getSubtotal()).isEqualTo(UPDATED_SUBTOTAL);
        assertThat(testPurchase.getDeliveryTotal()).isEqualTo(UPDATED_DELIVERY_TOTAL);
        assertThat(testPurchase.getTaxesTotal()).isEqualTo(UPDATED_TAXES_TOTAL);
        assertThat(testPurchase.getDiscountTotal()).isEqualTo(UPDATED_DISCOUNT_TOTAL);
        assertThat(testPurchase.getTotal()).isEqualTo(UPDATED_TOTAL);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchase() throws Exception {
        int databaseSizeBeforeUpdate = purchaseRepository.findAll().size();

        // Create the Purchase
        PurchaseDTO purchaseDTO = purchaseMapper.toDto(purchase);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseMockMvc.perform(put("/api/purchases")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Purchase in the database
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchase() throws Exception {
        // Initialize the database
        purchaseRepository.saveAndFlush(purchase);

        int databaseSizeBeforeDelete = purchaseRepository.findAll().size();

        // Delete the purchase
        restPurchaseMockMvc.perform(delete("/api/purchases/{id}", purchase.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Purchase> purchaseList = purchaseRepository.findAll();
        assertThat(purchaseList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Purchase.class);
        Purchase purchase1 = new Purchase();
        purchase1.setId(1L);
        Purchase purchase2 = new Purchase();
        purchase2.setId(purchase1.getId());
        assertThat(purchase1).isEqualTo(purchase2);
        purchase2.setId(2L);
        assertThat(purchase1).isNotEqualTo(purchase2);
        purchase1.setId(null);
        assertThat(purchase1).isNotEqualTo(purchase2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseDTO.class);
        PurchaseDTO purchaseDTO1 = new PurchaseDTO();
        purchaseDTO1.setId(1L);
        PurchaseDTO purchaseDTO2 = new PurchaseDTO();
        assertThat(purchaseDTO1).isNotEqualTo(purchaseDTO2);
        purchaseDTO2.setId(purchaseDTO1.getId());
        assertThat(purchaseDTO1).isEqualTo(purchaseDTO2);
        purchaseDTO2.setId(2L);
        assertThat(purchaseDTO1).isNotEqualTo(purchaseDTO2);
        purchaseDTO1.setId(null);
        assertThat(purchaseDTO1).isNotEqualTo(purchaseDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseMapper.fromId(null)).isNull();
    }
}
