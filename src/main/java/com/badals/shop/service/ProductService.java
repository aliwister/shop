package com.badals.shop.service;

import com.badals.shop.aop.logging.LocaleContext;
import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.graph.MerchantProductResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.ProductLangRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.search.ProductSearchRepository;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.SpeedDialDTO;
import com.badals.shop.service.mapper.AddProductMapper;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.service.pojo.AddProductDTO;

import com.badals.shop.service.util.ValidationUtil;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.*;
import com.badals.shop.xtra.ebay.EbayService;
import com.badals.shop.xtra.keepa.KeepaResponse;
import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.zip.CRC32;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8;


/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final AmazonPricingService amazonPricingService;
    private final PasUKService pasUKService;
    private final EbayService ebayService;

    private final ProductMapper productMapper;
    private final AddProductMapper addProductMapper;

    private final ProductSearchRepository productSearchRepository;
    private final SpeedDialService speedDialService;
    private final ProductIndexService productIndexService;
    private final ProductContentService productContentService;

    private final ProductLangRepository productLangRepository;
    private final Translate translateService;

    public static final long DEFAULT_WINDOW = 14400;

    public ProductService(ProductRepository productRepository, AmazonPricingService amazonPricingService, PasUKService pasUKService, EbayService ebayService, ProductMapper productMapper, AddProductMapper addProductMapper, ProductSearchRepository productSearchRepository, SpeedDialService speedDialService, ProductIndexService productIndexService, ProductContentService productContentService, ProductLangRepository productLangRepository, Translate translateService) {
        this.productRepository = productRepository;

        this.amazonPricingService = amazonPricingService;
        this.pasUKService = pasUKService;
        this.ebayService = ebayService;
        this.productMapper = productMapper;
        this.addProductMapper = addProductMapper;
        this.productSearchRepository = productSearchRepository;
        this.speedDialService = speedDialService;
        this.productIndexService = productIndexService;
        this.productContentService = productContentService;
        this.productLangRepository = productLangRepository;
        this.translateService = translateService;
    }

    /**
     * Save a product.
     *
     * @param productDTO the entity to save.
     * @return the persisted entity.
     */
    public ProductDTO save(ProductDTO productDTO) {
        log.debug("Request to save Product : {}", productDTO);
        Product product = productMapper.toEntity(productDTO);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }

    /**
     * Get all the products.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProductDTO> findAll() {
        log.debug("Request to get all Products");
        return productRepository.findAll().stream()
            .map(productMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public Optional<ProductDTO> findOne(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id)
            .map(productMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<Product> findOneEntity(Long id) {
        log.debug("Request to get Product : {}", id);
        return productRepository.findById(id);
    }

    public void delete(Long id) {
        log.debug("Request to delete Product : {}", id);
        productRepository.deleteById(id);
    }

    //@PreAuthorize("isAuthenticated()")
    public Optional<ProductDTO> getProduct(int id) {
        return this.findOne((long) id);
    }

    public Optional<ProductDTO> getProductAdmin(int id) {
        return this.findOne((long) id);
    }

    public List<ProductDTO> getAllProducts(int count) {
        return this.findAll();
    }

    public ProductDTO createProduct(Long ref, Long parent, String sku, String upc, LocalDate releaseDate) {
        Product product = new Product();
        product.ref(ref).sku(sku).upc(upc).releaseDate(releaseDate);
        return productMapper.toDto(product);
    }


    public ProductDTO getProductBySlug(String slug) throws ProductNotFoundException, NoOfferException, PricingException, ExecutionException, InterruptedException {
        char c = slug.charAt(0);
        if (c >= 'A' && c <= 'Z') {
            AddProductDTO addProductDTO = productSearchRepository.findBySlug(slug);
            if(addProductDTO != null)
                return addProductMapper.toProductDto(addProductDTO);
        }

        Product product = productRepository.findBySlugJoinCategories(slug).get();
        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() <1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().stream().filter(p -> p.getStub() == false).findFirst().get();
        }
        else if(product.getStub() != null && product.getStub()) {
            Product p = lookup(product.getSku());
            return this.getProductBySku(p, product.getSku());
        }

        if(product.getExpires() != null && product.getExpires().isBefore(Instant.now())) {
            Product p = lookup(product.getSku());
            return this.getProductBySku(p, product.getSku());
        }
        else if (product.getExpires() == null && product.getUpdated().isBefore(Instant.now().minusSeconds(DEFAULT_WINDOW))) {
            Product p = lookup(product.getSku());
            return this.getProductBySku(p, product.getSku());
        }

        return productRepository.findBySlugJoinCategories(product.getSlug()).map(productMapper::toDto).orElse(null);
    }


    public Product lookup(String sku) throws ExecutionException, InterruptedException, PricingException, NoOfferException {
/*
        return amazonPricingService.lookup(sku,false);
*/
        return null;
    }

/*
    public Mono<Product> lookupMono(String sku) throws ExecutionException, InterruptedException, PricingException, NoOfferException {
        return amazonPricingService.lookup(sku,false);
    }
*/

    public ProductResponse findAllByCategory(String slug, Integer offset, Integer limit) {
        List<Product> products = productRepository.findAllByCategorySlug(slug);
        ProductResponse response = new ProductResponse();
        response.setTotal(products.size());
        response.setItems(products.stream().map(productMapper::toDto).collect(Collectors.toList()));
        return response;
    }
    public List<ProductDTO> findRelated(String slug) {
        List<Product> products = productRepository.findAllByCategorySlug(slug);

        return products.stream().map(productMapper::toDto).collect(Collectors.toList());
    }

    @Transactional
    public ProductDTO getProductBySku(Product product, String sku) throws ProductNotFoundException, PricingException {
        //Product product = productRepository.findBySkuJoinCategories( sku).get();
        if(product == null )
            product = productRepository.findBySkuJoinCategories( sku).get();

        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() < 1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().iterator().next();
        }

        //if(product.getPrice() == null)
        //    throw new PricingException("Invalid price");
        //if(isSaveES)
        //productIndexService.saveToElastic(product);
        product = translateProduct(product);
        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        return productMapper.toDto(product);

    }

    private Product translateProduct(Product product) {
        String langCode = LocaleContext.getLocale();
        ProductLang lang = product.getProductLangs().stream().filter(x->x.getLang().equals(langCode)).findFirst().orElse(null);

        if(product.getVariationType() == VariationType.SIMPLE && lang != null)
            return product;

        ProductLang childLang = product.getProductLangs().stream().filter(x->x.getLang().equals(langCode)).findFirst().orElse(null);
        ProductLang childEn = product.getProductLangs().stream().filter(x->x.getLang().equals("en")).findFirst().orElse(null);
        if(childLang == null && childEn != null) {
            ProductLang target = new ProductLang();
            target = translateChild(childEn, target, langCode);
            if(product.getVariationType() == VariationType.SIMPLE)
                target = translateParent(childEn, target, langCode);
            product.addProductLang(target);
            product = productRepository.save(product);
        }

        if(product.getVariationType() == VariationType.SIMPLE) {
            return product;
        }

        ProductLang parentLang = product.getParent().getProductLangs().stream().filter(x->x.getLang().equals(langCode)).findFirst().orElse(null);
        if (parentLang != null)
            return product;

        ProductLang parentEn = product.getParent().getProductLangs().stream().filter(x->x.getLang().equals("en")).findFirst().orElse(null);
        if(parentLang == null && parentEn != null) {
            ProductLang target = new ProductLang();
            target = translateParent(parentEn, target, langCode);
            Product parent = product.getParent();
            parent.addProductLang(target);
            parent = productRepository.save(parent);
            return parent;
        }
        return product;
    }

    private ProductLang translateParent(ProductLang en, ProductLang target, String lang) {
        Translate.TranslateOption o = Translate.TranslateOption.targetLanguage(lang);
        if(en.getFeatures() != null)
            target.setFeatures( translateService.translate(en.getFeatures(),o).stream().map(Translation::getTranslatedText).collect(Collectors.toList()));
        if(en.getDescription() != null)
            target.setDescription(translateService.translate(en.getDescription(), o).getTranslatedText());
        target.setLang(lang);
        return target;
    }
    private ProductLang translateChild(ProductLang en, ProductLang target, String lang) {
        Translate.TranslateOption o = Translate.TranslateOption.targetLanguage(lang);
        target.setTitle(translateService.translate(en.getTitle(), o).getTranslatedText());
        target.setLang(lang);
        return target;
    }


    public ProductDTO getProductByDial(String dial) throws ProductNotFoundException, PricingException {
        Long ref = speedDialService.findRefByDial(dial);
        return productRepository.findBySlugJoinCategories(String.valueOf(ref)).map(productMapper::toDto).orElse(null);
    }

    //public static final String LATEST = "LATEST";
   //@Cacheable(cacheNames = LATEST)
   public ProductResponse getLatest(Integer limit) {
       List<Product> products = productRepository.findByVariationTypeInAndPriceIsNotNullAndStubEqualsAndHashtagsIsNotNullOrderByCreatedDesc(Arrays.asList(new VariationType[]{VariationType.SIMPLE}), PageRequest.of(0,20), false);
       ProductResponse response = new ProductResponse();
       response.setTotal(products.size());
       response.setItems(products.stream().map(productMapper::toDto).collect(Collectors.toList()));
       return response;
   }

   public MerchantProductResponse getForTenant(Long tenantId, Integer limit, Integer offset) {
       Integer total = productRepository.countForTenant(tenantId);
       List<Product> products = productRepository.listForTenantAll(tenantId, PageRequest.of((int) offset/limit,limit));
       MerchantProductResponse response = new MerchantProductResponse();
       response.setTotal(total);
       response.setHasMore((limit+offset) < total);
       response.setItems(products.stream().map(addProductMapper::toDto).collect(Collectors.toList()));
       return response;
   }


    public ProductDTO lookupPas(String sku, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException, ExecutionException, InterruptedException {
        //Product p = this.amazonPricingService.lookup(sku, true);
/*        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            productIndexService.indexProduct(p.getId());*/

        Product p = lookup(sku);
        return this.getProductBySku(p, sku);

        //throw new PricingException("Bummer");
    }

    public Mono<ProductDTO> lookupMono(String sku, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException, ExecutionException, InterruptedException {
        return Mono.just(new ProductDTO());
    }

/*
    public ProductDTO lookupForcePas(String sku, boolean isParent, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException {
        Product p = this.pas5Service.lookup(sku, isParent, isRedis, isRebuild, true);
        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            productIndexService.indexProduct(p.getId());
        return this.getProductBySku(sku);
    }
*/

    public ProductDTO lookupForcePasUk(String sku, boolean isParent, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException {
        Product p = this.pasUKService.lookup(sku, isParent, isRedis, isRebuild, true);
        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            productIndexService.indexProduct(p.getId());
        return this.getProductBySku(p, sku);
    }

    public String getParentOf(String sku) {
        return productRepository.findOneBySkuAndMerchantId(sku, 1L).get().getParent().getSku();
    }

    public AddProductDTO createMerchantProduct(AddProductDTO dto, Long currentMerchantId, String currentMerchant, Long tenantId, String currentTenant) {

        if (dto.getSalePrice() != null) {
            int discount = 100 * (int) ((dto.getPrice().doubleValue() - dto.getSalePrice().doubleValue()) / dto.getPrice().doubleValue());
            dto.setDiscountInPercent(discount);
        }
        dto.setTenant(currentTenant);
        dto.setMerchant(currentMerchant);
        dto.setImported(false);
        dto.setIndexed(false);
        productIndexService.saveToElastic(dto);
        return dto;
    }

    public AddProductDTO createProduct(AddProductDTO dto, boolean isSaveES, Long currentMerchantId) {
        Product product = productRepository.findById(dto.getId()).orElse(new Product());
        CRC32 checksum = new CRC32();
        if(dto.getVariationType() == null)
            dto.setType("SIMPLE");

        if(dto.getId() == null) {
            product = addProductMapper.toEntity(dto);

            product.setSku(dto.getSku());
            checksum.update(dto.getSku().getBytes());
            String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());
            product.setRef(Long.valueOf(ref));
            product.setSlug(ref);
        }
        else {
            product = productRepository.findOneByRef(dto.getId()).get();
            if(dto.getRef() == null || dto.getRef().equals("")) {
                String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());
                product.setRef(Long.valueOf(ref));
                product.setSlug(ref);
            }
        }

        product.setActive(true);
        product.setStub(false);
        product.setInStock(true);
        product.setCurrency("OMR");


/*        product.setRef(dto.getRef());
        product.setSlug(dto.getSlug());*/
        product.setTitle(dto.getName());
        product.setMerchantId(currentMerchantId);


        product.getMerchantStock().clear();
        product.getProductLangs().clear();
        product.getCategories().clear();

/*
        for(Long id: dto.getShopIds()) {
            product.getCategories().add(new Category(id));
        }
*/

        //product.getProductLangs().add(new ProductLang().title("Fuck").product(product).lang("ar"));
        int discount = 0;

/*        if(dto.getSalePrice() != null)
            discount = 100 * (int)((dto.getPrice().doubleValue() - dto.getSalePrice().doubleValue())/dto.getPrice().doubleValue());*/

        product.getMerchantStock().add(new MerchantStock().quantity(dto.getQuantity()).availability(dto.getAvailability()).cost(dto.getCost()).allow_backorder(false)
                .price(dto.getSalePrice()).discount(discount).product(product).merchantId(currentMerchantId));

        ProductLang langAr = new ProductLang().lang("ar").description(dto.getDescription_ar()).title(dto.getName_ar()).brand(dto.getBrand_ar()).browseNode(dto.getBrowseNode());
        if(dto.getFeatures_ar() != null)
            langAr.setFeatures(Arrays.asList(dto.getFeatures_ar().split(";")));


        ProductLang langEn = new ProductLang().lang("en").description(dto.getDescription()).title(dto.getName()).brand(dto.getBrand()).browseNode(dto.getBrowseNode());
        if(dto.getFeatures() != null)
            langEn.setFeatures(Arrays.asList(dto.getFeatures().split(";")));

        product.getProductLangs().add(langAr.product(product));
        product.getProductLangs().add(langEn.product(product));

        if(dto.getUrl() != null && ValidationUtil.isValidURL(dto.getUrl())) {
            product.setUrl(dto.getUrl());
        }
        else
            product.setUrl(null);

        //product.ref(ref).sku(sku).upc(upc).releaseDate(releaseDate);
        product = productRepository.save(product);
        //dto.setTenant(currentMerchant);
        //dto.setSlug(product.getSlug());
        //dto.setRef(product.getRef());
        //dto.setImported(true);


        if(dto.getDial() != null && dto.getDial().startsWith("*")) {
            speedDialService.save(new SpeedDialDTO().dial(dto.getDial()).ref(product.getRef()).expires(Instant.now()));
        }

        if(isSaveES)
            productIndexService.saveToElastic(dto);
        return  addProductMapper.toDto(product);
    }
    public AddProductDTO createStub(AddProductDTO dto, boolean isSaveES, Long currentMerchantId) throws Exception {

        Optional<Product> productOptional = null;
        if(dto.getId() != null)
            productOptional = productRepository.findById(dto.getId());
        else
            productOptional = productRepository.findOneBySkuAndMerchantId(dto.getSku(), dto.getMerchantId());

        Product product = null;
        if(!productOptional.isPresent()) {
            CRC32 checksum = new CRC32();
            product = addProductMapper.toEntity(dto);

            product.setSku(dto.getSku());
            checksum.update(dto.getSku().getBytes());
            String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());
            product.setRef(Long.valueOf(ref));
            product.setSlug(ref);
            product.setStub(true);
        }
        else
            product = productOptional.get();

        if(!product.getStub())
            throw new Exception("Can't create a stub product from a product that already exists");


        if(dto.getVariationType() == null)
            dto.setType("SIMPLE");

        product.setActive(false);
        product.setInStock(false);
        product.setCurrency("OMR");


/*        product.setRef(dto.getRef());
        product.setSlug(dto.getSlug());*/
        product.setTitle(dto.getName());
        product.setMerchantId(currentMerchantId);


        product.getMerchantStock().clear();
        product.getProductLangs().clear();
        product.getCategories().clear();

        int discount = 0;

        if(dto.getUrl() != null && ValidationUtil.isValidURL(dto.getUrl())) {
            product.setUrl(dto.getUrl());
        }
        else
            product.setUrl(null);

        product = productRepository.save(product);

        dto = addProductMapper.toDto(product);
        if(isSaveES)
            productIndexService.saveToElastic(dto);
        return  addProductMapper.toDto(product);
    }

    public ProductDTO lookupEbay(String id) throws NoOfferException, ProductNotFoundException, PricingException {
        Product node = ebayService.lookup(id, false);
        //productSearchRepository.save(addProductMapper.toDto(node));
        productIndexService.saveToElastic(node);
        return productMapper.toDto(node);
    }

    public boolean exists(Long productId) {
        if (productRepository.findOneByRef(productId).isPresent())
            return true;

        if (productSearchRepository.existsById(productId)) {
            productRepository.save(addProductMapper.toEntity(productSearchRepository.findById(productId).get()).active(true));
            return true;
        }

        return false;
    }

    public ProductDTO getProductFromSearch(ProductDTO dto) {
        Product product = productMapper.toEntity(dto);
        product = amazonPricingService.initSearchStub(product, AmazonPricingService.AMAZON_US_MERCHANT_ID);
        product =  productRepository.save(product);
        return productMapper.toDto(product);
    }
/*
    public <U> U log(Customer loginUser, String slug, cookie) {
       productRepository.log(loginUser.getId(), slug, cookie);
    }
*/

//    public List<ProductDTO> listForMerchantsAll(Long merchantId) {
//        return productRepository.listForMerchantsAll(merchantId).stream().map(productMapper::toDto).collect(Collectors.toList());
//    }
}
