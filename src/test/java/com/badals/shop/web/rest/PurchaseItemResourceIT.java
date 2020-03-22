package com.badals.shop.web.rest;

import com.badals.shop.ShopApp;
import com.badals.shop.domain.PurchaseItem;
import com.badals.shop.domain.Purchase;
import com.badals.shop.repository.PurchaseItemRepository;
import com.badals.shop.service.PurchaseItemService;
import com.badals.shop.service.dto.PurchaseItemDTO;
import com.badals.shop.service.mapper.PurchaseItemMapper;
import com.badals.shop.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import java.util.ArrayList;
import java.util.List;

import static com.badals.shop.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@Link PurchaseItemResource} REST controller.
 */
@SpringBootTest(classes = ShopApp.class)
public class PurchaseItemResourceIT {

    private static final Integer DEFAULT_SEQUENCE = 1;
    private static final Integer UPDATED_SEQUENCE = 2;

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);

    private static final LocalDate DEFAULT_ESTIMATED_DELIVERY_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_ESTIMATED_DELIVERY_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_SHIPPING_INSTRUCTIONS = "AAAAAAAAAA";
    private static final String UPDATED_SHIPPING_INSTRUCTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    @Autowired
    private PurchaseItemRepository purchaseItemRepository;

    @Mock
    private PurchaseItemRepository purchaseItemRepositoryMock;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    @Mock
    private PurchaseItemService purchaseItemServiceMock;

    @Autowired
    private PurchaseItemService purchaseItemService;

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

    private MockMvc restPurchaseItemMockMvc;

    private PurchaseItem purchaseItem;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PurchaseItemResource purchaseItemResource = new PurchaseItemResource(purchaseItemService);
        this.restPurchaseItemMockMvc = MockMvcBuilders.standaloneSetup(purchaseItemResource)
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
    public static PurchaseItem createEntity(EntityManager em) {
        PurchaseItem purchaseItem = new PurchaseItem()
            .sequence(DEFAULT_SEQUENCE)
            .quantity(DEFAULT_QUANTITY)
            .price(DEFAULT_PRICE)
            .estimatedDeliveryDate(DEFAULT_ESTIMATED_DELIVERY_DATE)
            .shippingInstructions(DEFAULT_SHIPPING_INSTRUCTIONS)
            .description(DEFAULT_DESCRIPTION)
            .comment(DEFAULT_COMMENT);
        // Add required entity
        Purchase purchase;
        if (TestUtil.findAll(em, Purchase.class).isEmpty()) {
            purchase = PurchaseResourceIT.createEntity(em);
            em.persist(purchase);
            em.flush();
        } else {
            purchase = TestUtil.findAll(em, Purchase.class).get(0);
        }
        purchaseItem.setPurchase(purchase);
        return purchaseItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PurchaseItem createUpdatedEntity(EntityManager em) {
        PurchaseItem purchaseItem = new PurchaseItem()
            .sequence(UPDATED_SEQUENCE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .shippingInstructions(UPDATED_SHIPPING_INSTRUCTIONS)
            .description(UPDATED_DESCRIPTION)
            .comment(UPDATED_COMMENT);
        // Add required entity
        Purchase purchase;
        if (TestUtil.findAll(em, Purchase.class).isEmpty()) {
            purchase = PurchaseResourceIT.createUpdatedEntity(em);
            em.persist(purchase);
            em.flush();
        } else {
            purchase = TestUtil.findAll(em, Purchase.class).get(0);
        }
        purchaseItem.setPurchase(purchase);
        return purchaseItem;
    }

    @BeforeEach
    public void initTest() {
        purchaseItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createPurchaseItem() throws Exception {
        int databaseSizeBeforeCreate = purchaseItemRepository.findAll().size();

        // Create the PurchaseItem
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);
        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isCreated());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeCreate + 1);
        PurchaseItem testPurchaseItem = purchaseItemList.get(purchaseItemList.size() - 1);
        assertThat(testPurchaseItem.getSequence()).isEqualTo(DEFAULT_SEQUENCE);
        assertThat(testPurchaseItem.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testPurchaseItem.getPrice()).isEqualTo(DEFAULT_PRICE);
        assertThat(testPurchaseItem.getEstimatedDeliveryDate()).isEqualTo(DEFAULT_ESTIMATED_DELIVERY_DATE);
        assertThat(testPurchaseItem.getShippingInstructions()).isEqualTo(DEFAULT_SHIPPING_INSTRUCTIONS);
        assertThat(testPurchaseItem.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPurchaseItem.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void createPurchaseItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = purchaseItemRepository.findAll().size();

        // Create the PurchaseItem with an existing ID
        purchaseItem.setId(1L);
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPurchaseItemMockMvc.perform(post("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPurchaseItems() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);

        // Get all the purchaseItemList
        restPurchaseItemMockMvc.perform(get("/api/purchase-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(purchaseItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].sequence").value(hasItem(DEFAULT_SEQUENCE)))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(DEFAULT_PRICE.intValue())))
            .andExpect(jsonPath("$.[*].estimatedDeliveryDate").value(hasItem(DEFAULT_ESTIMATED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].shippingInstructions").value(hasItem(DEFAULT_SHIPPING_INSTRUCTIONS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllPurchaseItemsWithEagerRelationshipsIsEnabled() throws Exception {
        PurchaseItemResource purchaseItemResource = new PurchaseItemResource(purchaseItemServiceMock);
        when(purchaseItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        MockMvc restPurchaseItemMockMvc = MockMvcBuilders.standaloneSetup(purchaseItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPurchaseItemMockMvc.perform(get("/api/purchase-items?eagerload=true"))
        .andExpect(status().isOk());

        verify(purchaseItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllPurchaseItemsWithEagerRelationshipsIsNotEnabled() throws Exception {
        PurchaseItemResource purchaseItemResource = new PurchaseItemResource(purchaseItemServiceMock);
            when(purchaseItemServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));
            MockMvc restPurchaseItemMockMvc = MockMvcBuilders.standaloneSetup(purchaseItemResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();

        restPurchaseItemMockMvc.perform(get("/api/purchase-items?eagerload=true"))
        .andExpect(status().isOk());

            verify(purchaseItemServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getPurchaseItem() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);

        // Get the purchaseItem
        restPurchaseItemMockMvc.perform(get("/api/purchase-items/{id}", purchaseItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(purchaseItem.getId().intValue()))
            .andExpect(jsonPath("$.sequence").value(DEFAULT_SEQUENCE))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.price").value(DEFAULT_PRICE.intValue()))
            .andExpect(jsonPath("$.estimatedDeliveryDate").value(DEFAULT_ESTIMATED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.shippingInstructions").value(DEFAULT_SHIPPING_INSTRUCTIONS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPurchaseItem() throws Exception {
        // Get the purchaseItem
        restPurchaseItemMockMvc.perform(get("/api/purchase-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePurchaseItem() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);

        int databaseSizeBeforeUpdate = purchaseItemRepository.findAll().size();

        // Update the purchaseItem
        PurchaseItem updatedPurchaseItem = purchaseItemRepository.findById(purchaseItem.getId()).get();
        // Disconnect from session so that the updates on updatedPurchaseItem are not directly saved in db
        em.detach(updatedPurchaseItem);
        updatedPurchaseItem
            .sequence(UPDATED_SEQUENCE)
            .quantity(UPDATED_QUANTITY)
            .price(UPDATED_PRICE)
            .estimatedDeliveryDate(UPDATED_ESTIMATED_DELIVERY_DATE)
            .shippingInstructions(UPDATED_SHIPPING_INSTRUCTIONS)
            .description(UPDATED_DESCRIPTION)
            .comment(UPDATED_COMMENT);
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(updatedPurchaseItem);

        restPurchaseItemMockMvc.perform(put("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isOk());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeUpdate);
        PurchaseItem testPurchaseItem = purchaseItemList.get(purchaseItemList.size() - 1);
        assertThat(testPurchaseItem.getSequence()).isEqualTo(UPDATED_SEQUENCE);
        assertThat(testPurchaseItem.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testPurchaseItem.getPrice()).isEqualTo(UPDATED_PRICE);
        assertThat(testPurchaseItem.getEstimatedDeliveryDate()).isEqualTo(UPDATED_ESTIMATED_DELIVERY_DATE);
        assertThat(testPurchaseItem.getShippingInstructions()).isEqualTo(UPDATED_SHIPPING_INSTRUCTIONS);
        assertThat(testPurchaseItem.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPurchaseItem.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPurchaseItem() throws Exception {
        int databaseSizeBeforeUpdate = purchaseItemRepository.findAll().size();

        // Create the PurchaseItem
        PurchaseItemDTO purchaseItemDTO = purchaseItemMapper.toDto(purchaseItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPurchaseItemMockMvc.perform(put("/api/purchase-items")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(purchaseItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PurchaseItem in the database
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePurchaseItem() throws Exception {
        // Initialize the database
        purchaseItemRepository.saveAndFlush(purchaseItem);

        int databaseSizeBeforeDelete = purchaseItemRepository.findAll().size();

        // Delete the purchaseItem
        restPurchaseItemMockMvc.perform(delete("/api/purchase-items/{id}", purchaseItem.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PurchaseItem> purchaseItemList = purchaseItemRepository.findAll();
        assertThat(purchaseItemList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseItem.class);
        PurchaseItem purchaseItem1 = new PurchaseItem();
        purchaseItem1.setId(1L);
        PurchaseItem purchaseItem2 = new PurchaseItem();
        purchaseItem2.setId(purchaseItem1.getId());
        assertThat(purchaseItem1).isEqualTo(purchaseItem2);
        purchaseItem2.setId(2L);
        assertThat(purchaseItem1).isNotEqualTo(purchaseItem2);
        purchaseItem1.setId(null);
        assertThat(purchaseItem1).isNotEqualTo(purchaseItem2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PurchaseItemDTO.class);
        PurchaseItemDTO purchaseItemDTO1 = new PurchaseItemDTO();
        purchaseItemDTO1.setId(1L);
        PurchaseItemDTO purchaseItemDTO2 = new PurchaseItemDTO();
        assertThat(purchaseItemDTO1).isNotEqualTo(purchaseItemDTO2);
        purchaseItemDTO2.setId(purchaseItemDTO1.getId());
        assertThat(purchaseItemDTO1).isEqualTo(purchaseItemDTO2);
        purchaseItemDTO2.setId(2L);
        assertThat(purchaseItemDTO1).isNotEqualTo(purchaseItemDTO2);
        purchaseItemDTO1.setId(null);
        assertThat(purchaseItemDTO1).isNotEqualTo(purchaseItemDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(purchaseItemMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(purchaseItemMapper.fromId(null)).isNull();
    }
}
