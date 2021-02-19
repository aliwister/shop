package com.badals.shop.service;

import com.algolia.search.SearchIndex;
import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.MerchantProductResponse;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.repository.ProductLangRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.search.ProductSearchRepository;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.SpeedDialDTO;
import com.badals.shop.service.dto.TenantDTO;
import com.badals.shop.service.mapper.AddProductMapper;
import com.badals.shop.service.mapper.AlgoliaProductMapper;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.service.pojo.AddProductDTO;

import com.badals.shop.service.util.ValidationUtil;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PasUKService;
import com.badals.shop.xtra.amazon.PricingException;
import com.badals.shop.xtra.ebay.EbayService;
import org.imgscalr.Scalr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.springframework.context.MessageSource;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;

import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Instant;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import java.util.zip.CRC32;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepository productRepository;
    private final SearchIndex<AlgoliaProduct> index;
    private final Pas5Service pas5Service;
    private final PasUKService pasUKService;
    private final EbayService ebayService;
    private final AwsService awsService;
    private final MessageSource messageSource;

    private final ProductMapper productMapper;
    private final AlgoliaProductMapper algoliaProductMapper;
    private final AddProductMapper addProductMapper;

    private final ProductSearchRepository productSearchRepository;
    private final TenantService tenantService;
    private final SpeedDialService speedDialService;

    public ProductService(ProductRepository productRepository, PasUKService pasUKService, EbayService ebayService, ProductMapper productMapper, AlgoliaProductMapper algoliaProductMapper, SearchIndex<AlgoliaProduct> index, Pas5Service pas5Service, AwsService awsService, MessageSource messageSource, AddProductMapper addProductMapper, ProductLangRepository productLangRepository, ProductSearchRepository productSearchRepository, TenantService tenantService, SpeedDialService speedDialService) {
        this.productRepository = productRepository;
        this.pasUKService = pasUKService;
        this.ebayService = ebayService;
        this.productMapper = productMapper;
        this.algoliaProductMapper = algoliaProductMapper;
        this.index = index;
        this.pas5Service = pas5Service;
        this.awsService = awsService;
        this.messageSource = messageSource;
        this.addProductMapper = addProductMapper;

        this.productLangRepository = productLangRepository;
        this.productSearchRepository = productSearchRepository;
        this.tenantService = tenantService;
        this.speedDialService = speedDialService;
    }

    @Transactional(readOnly = true)
    public List<AddProductDTO> search(String query) {
        log.debug("Request to search ShipmentItems for query {}", query);
        return StreamSupport
                .stream(productSearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AddProductDTO> searchPageable(String query, Integer page, Integer pageSize) {
        log.debug("Request to search ShipmentItems for query {}", query);
        return StreamSupport
                .stream(productSearchRepository.search(queryStringQuery(query), PageRequest.of(page, pageSize)).spliterator(), false).collect(Collectors.toList());
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


    /**
     * Get one product by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
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

    /**
     * Delete the product by id.
     *
     * @param id the id of the entity.
     */
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

    public ProductDTO createNewProduct(ProductDTO product) {
        ProductDTO product2 = save(product);

        return product2;
    }

    final
    ProductLangRepository productLangRepository;

    public Attribute indexProduct(long id) {
        Product product = productRepository.getOne(id);
       // AlgoliaProduct algoliaProduct = algoliaProductMapper.producttoAlgoliaProduct(product);

        /*for(ProductLang i: productLangRepository.findAllByProductId( id ) ){
            algoliaProduct.getI18().put(i.getLang(), new ProductI18(i.getTitle(), "", ""));
        }*/
       // index.saveObject(algoliaProduct);
        return new Attribute("success", "1");
    }

    public ProductDTO getProductBySlug(String slug) throws ProductNotFoundException, NoOfferException, PricingException {
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
            //product.setStub(false);
            //product.setPrice(new Price(BigDecimal.TEN,"OMR"));
            //productRepository.save(product);
            pas5Service.lookup(product.getSku(),false, false, false, false);
            return this.getProductBySku(product.getSku());
        }

        return productRepository.findBySlugJoinCategories(product.getSlug()).map(productMapper::toDto).orElse(null);
        //return productRepository.findBySlugJoinCategories(slug).map(productMapper::toDto).orElse(null);
    }

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

    public ProductDTO getProductBySku(String sku) throws ProductNotFoundException, PricingException {
        Product product = productRepository.findBySkuJoinCategories( sku).get();
        if(product == null )
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() < 1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().iterator().next();
        }

        //if(product.getPrice() == null)
        //    throw new PricingException("Invalid price");
        //if(isSaveES)
        saveToElastic(product);
        return productRepository.findBySlugJoinCategories(product.getSlug()).map(productMapper::toDto).orElse(null);

    }

    public ProductDTO getProductByDial(String dial) throws ProductNotFoundException, PricingException {
        Long ref = speedDialService.findRefByDial(dial);
        return productRepository.findBySlugJoinCategories(String.valueOf(ref)).map(productMapper::toDto).orElse(null);
    }

    //public static final String LATEST = "LATEST";
   //@Cacheable(cacheNames = LATEST)
   public ProductResponse getLatest(Integer limit) {
       List<Product> products = productRepository.findByVariationTypeInAndPriceIsNotNullOrderByCreatedDesc(Arrays.asList(new VariationType[]{VariationType.SIMPLE}), PageRequest.of(0,20));
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

    public ProductDTO lookupPas(String sku, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException {
        return lookupPas(sku, false, isRedis, isRebuild);
    }

    public ProductDTO lookupPas(String sku, boolean isParent, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException {
        Product p = this.pas5Service.lookup(sku, isParent, isRedis, isRebuild, false);
        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            this.indexProduct(p.getId());
        return this.getProductBySku(sku);
    }

    public ProductDTO lookupForcePas(String sku, boolean isParent, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException {
        Product p = this.pas5Service.lookup(sku, isParent, isRedis, isRebuild, true);
        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            this.indexProduct(p.getId());
        return this.getProductBySku(sku);
    }

    public ProductDTO lookupForcePasUk(String sku, boolean isParent, boolean isRedis, boolean isRebuild) throws ProductNotFoundException, PricingException, NoOfferException {
        Product p = this.pasUKService.lookup(sku, isParent, isRedis, isRebuild, true);
        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            this.indexProduct(p.getId());
        return this.getProductBySku(sku);
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
        saveToElastic(dto);
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
            saveToElastic(dto);
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

/*
        for(Long id: dto.getShopIds()) {
            product.getCategories().add(new Category(id));
        }
*/

        //product.getProductLangs().add(new ProductLang().title("Fuck").product(product).lang("ar"));
        int discount = 0;

/*        if(dto.getSalePrice() != null)
            discount = 100 * (int)((dto.getPrice().doubleValue() - dto.getSalePrice().doubleValue())/dto.getPrice().doubleValue());*/

/*        product.getMerchantStock().add(new MerchantStock().quantity(dto.getQuantity()).availability(dto.getAvailability()).cost(dto.getCost()).allow_backorder(false)
                .price(dto.getSalePrice()).discount(discount).product(product).merchantId(currentMerchantId));

        ProductLang langAr = new ProductLang().lang("ar").description(dto.getDescription_ar()).title(dto.getName_ar()).brand(dto.getBrand_ar()).browseNode(dto.getBrowseNode());
        if(dto.getFeatures_ar() != null)
            langAr.setFeatures(Arrays.asList(dto.getFeatures_ar().split(";")));


        ProductLang langEn = new ProductLang().lang("en").description(dto.getDescription()).title(dto.getName()).brand(dto.getBrand()).browseNode(dto.getBrowseNode());
        if(dto.getFeatures() != null)
            langEn.setFeatures(Arrays.asList(dto.getFeatures().split(";")));

        product.getProductLangs().add(langAr.product(product));
        product.getProductLangs().add(langEn.product(product));*/

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

/*

        if(dto.getDial() != null && dto.getDial().startsWith("*")) {
            speedDialService.save(new SpeedDialDTO().dial(dto.getDial()).ref(product.getRef()).expires(Instant.now()));
        }
*/
        dto = addProductMapper.toDto(product);
        if(isSaveES)
            saveToElastic(dto);
        return  addProductMapper.toDto(product);
    }



    public void addToElastic(Long id, String sku, String name, String name_ar, List<String> shops) {
        productSearchRepository.index(new AddProductDTO(id,
                sku,
                name,
                name_ar,
                shops));
    }

    public void importProducts(List<AddProductDTO> products, Long currentMerchantId, String currentMerchant, Long tenantId, String currentTenant, List<Long> shopIds, String browseNode) {
       TenantDTO tenantObj = tenantService.findOne(tenantId).get();

       for(AddProductDTO doc: products) {
            Long id = doc.getId();
            doc.setId(null);
            if (doc.getImage() != null ) {
                String image = uploadToS3(doc.getImage(), currentMerchantId, currentMerchant, tenantId);
                doc.setImage(image);
            }
            if(!doc.getSku().startsWith(tenantObj.getSkuPrefix()))
                doc.setSku(tenantObj.getSkuPrefix()+doc.getSku());
            //doc.setShopIds(shopIds);
            //doc.setBrowseNode(browseNode);
            //importMerchantProducts(doc, currentMerchantId, currentMerchant, tenantId, currentTenant, false);
            doc.setId(id);
            doc.setImported(true);
            doc.setMerchant(currentMerchant);
            doc.setTenant(currentTenant);
            AlgoliaProduct algoliaProduct = algoliaProductMapper.addProductToAlgoliaProduct(doc);

            saveToElastic(doc);
           index.saveObject(algoliaProduct);

        }
    }

    private String uploadToS3(String image, Long currentMerchantId, String currentMerchant, Long tenantId) {
        String t = TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        CRC32 checksum = new CRC32();
        checksum.update(image.getBytes());

        String objectKey = "_m/" + m + "/" + checksum.getValue()+image.substring(image.length()-4,image.length());


        try {
            //PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key().contentType().build();
            BufferedImage img = ImageIO.read(new URL(image));
            if(img.getHeight() > 350 || img.getWidth() > 350)
                img = resizeAndCrop(img, 300, 300);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img,"png", outputStream);
            PutObjectResponse response = awsService.getS3Client().putObject(PutObjectRequest.builder().bucket(awsService.getBucketName()).key(objectKey).build(), RequestBody.fromBytes(outputStream.toByteArray()));
            return "https://cdn.badals.com/"+ objectKey.substring(3);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public MerchantProductResponse searchForTenant(String currentTenant, String text, Integer limit, Integer offset, Boolean imported) {
        List<AddProductDTO> result = search("tenant:"+currentTenant + " AND imported:" + imported.toString() + ((text != null)?" AND "+text:""));
        MerchantProductResponse response = new MerchantProductResponse();
        response.setTotal(12);
        response.setHasMore((limit+offset) < 12);
        response.setItems(result);
        return response;
    }

    public ProductResponse searchAll(String type) {
        List<AddProductDTO> result = search(type + " AND imported:true ");
        ProductResponse response = new ProductResponse();
        response.setTotal(12);
        //response.setHasMore((limit+offset) < 12);
        response.setItems(result.stream().map(addProductMapper::toProductDto).collect(Collectors.toList()));
        return response;
    }

    public BufferedImage resizeAndCrop(BufferedImage bufferedImage, Integer width, Integer height) {

        Scalr.Mode mode = (double) width / (double) height >= (double) bufferedImage.getWidth() / (double) bufferedImage.getHeight() ? Scalr.Mode.FIT_TO_WIDTH
                : Scalr.Mode.FIT_TO_HEIGHT;

        bufferedImage = Scalr.resize(bufferedImage, Scalr.Method.ULTRA_QUALITY, mode, width, height);

        int x = 0;
        int y = 0;

        if (mode == Scalr.Mode.FIT_TO_WIDTH) {
            y = (bufferedImage.getHeight() - height) / 2;
        } else if (mode == Scalr.Mode.FIT_TO_HEIGHT) {
            x = (bufferedImage.getWidth() - width) / 2;
        }

        bufferedImage = Scalr.crop(bufferedImage, x, y, width, height);

        return bufferedImage;
    }

    public static Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    public ProductDTO lookupEbay(String id) throws NoOfferException, ProductNotFoundException, PricingException {
        Product node = ebayService.lookup(id, false);
        //productSearchRepository.save(addProductMapper.toDto(node));
        saveToElastic(node);
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

    public ProductResponse findByType(String type) {
        List<AddProductDTO> result = search(type + " AND imported:true");
        ProductResponse response = new ProductResponse();
        response.setTotal(6);
        response.setHasMore(false);
        response.setItems(result.stream().map(addProductMapper::toProductDto).collect(Collectors.toList()));
        return response;
    }

    public ProductResponse findByHashtag(String hashtag) {
        List<AddProductDTO> result = search(hashtag );
        ProductResponse response = new ProductResponse();
        response.setTotal(6);
        response.setHasMore(false);
        response.setItems(result.stream().map(addProductMapper::toProductDto).collect(Collectors.toList()));
        return response;
    }

    public ProductResponse findByKeyword(String keyword) {
        List<AddProductDTO> result = searchPageable(keyword, 0, 10 );
        ProductResponse response = new ProductResponse();
        response.setTotal(result.size());
        response.setHasMore(false);
        response.setItems(result.stream().map(addProductMapper::toProductDto).collect(Collectors.toList()));
        return response;
    }

    public void setHashtags(List<String> hashs, Long ref) throws ProductNotFoundException {
        Product p = productRepository.findOneByRef(ref).orElse(null);
        if (p == null)
            throw new ProductNotFoundException("No product found for ref "+ref);

        p.setHashtags(hashs);
        productRepository.save(p);
        //productSearchRepository.save(addProductMapper.toDto(p));
        saveToElastic(p);
    }

    private void saveToElastic(Product product) {
        AddProductDTO dto = addProductMapper.toDto(product);
        saveToElastic(dto);
    }
    protected void saveToElastic(AddProductDTO dto) {
        dto.setId(dto.getRef());
        productSearchRepository.save(dto);
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
