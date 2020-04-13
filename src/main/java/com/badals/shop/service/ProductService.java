package com.badals.shop.service;

import com.algolia.search.SearchIndex;
import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.*;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.MerchantProductResponse;
import com.badals.shop.domain.pojo.ProductI18;
import com.badals.shop.domain.pojo.ProductResponse;
import com.badals.shop.repository.ProductLangRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.search.ProductSearchRepository;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.AddProductMapper;
import com.badals.shop.service.mapper.AlgoliaProductMapper;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.service.pojo.AddProductDTO;

import com.badals.shop.service.util.S3Util;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import org.elasticsearch.index.query.QueryBuilders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.util.*;
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
    private final MessageSource messageSource;

    private final ProductMapper productMapper;
    private final AlgoliaProductMapper algoliaProductMapper;
    private final AddProductMapper addProductMapper;

    private final ProductSearchRepository productSearchRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, AlgoliaProductMapper algoliaProductMapper, SearchIndex<AlgoliaProduct> index, Pas5Service pas5Service, MessageSource messageSource, AddProductMapper addProductMapper, ProductLangRepository productLangRepository, ProductSearchRepository productSearchRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.algoliaProductMapper = algoliaProductMapper;
        this.index = index;
        this.pas5Service = pas5Service;
        this.messageSource = messageSource;
        this.addProductMapper = addProductMapper;
        this.productLangRepository = productLangRepository;
        this.productSearchRepository = productSearchRepository;
    }

    @Transactional(readOnly = true)
    public List<AddProductDTO> search(String query) {
        log.debug("Request to search ShipmentItems for query {}", query);
        return StreamSupport
                .stream(productSearchRepository.search(queryStringQuery(query)).spliterator(), false).collect(Collectors.toList());
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
        AlgoliaProduct algoliaProduct = algoliaProductMapper.producttoAlgoliaProduct(product);

        /*for(ProductLang i: productLangRepository.findAllByProductId( id ) ){
            algoliaProduct.getI18().put(i.getLang(), new ProductI18(i.getTitle(), "", ""));
        }*/
        index.saveObject(algoliaProduct);
        return new Attribute("success", "1");
    }

    public ProductDTO getProductBySlug(String slug) throws ProductNotFoundException {
        Product product = productRepository.findBySlugJoinCategories(slug).get();
        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() <1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().iterator().next();
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

        return productRepository.findBySlugJoinCategories(product.getSlug()).map(productMapper::toDto).orElse(null);

    }

   // @Cacheable(value="latest-products")
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
        Product p = this.pas5Service.lookup(sku, isParent, isRedis, isRebuild);
        if(p.getVariationType() == VariationType.SIMPLE && p.getPrice() != null)
            this.indexProduct(p.getId());
        return this.getProductBySku(sku);
    }


    public String getParentOf(String sku) {
        return productRepository.findOneBySku(sku).get().getParent().getSku();
    }

    public AddProductDTO createMerchantProduct(AddProductDTO dto, Long currentMerchantId, String currentMerchant, Long tenantId) {
        Product product = null;
        if(dto.getId() == null) {
            product = addProductMapper.toEntity(dto);

            product.setSku(currentMerchant + "-" + dto.getSku());
            CRC32 checksum = new CRC32();
            checksum.update(dto.getSku().getBytes());
            String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());
            product.setRef(Long.valueOf(ref));
            product.setSlug(ref);
        }
        else
            product = productRepository.findById(dto.getId()).get();


        product.setActive(false);


        product.setTitle(dto.getName());
        product.setTenantId(tenantId);
        product.getMerchantStock().clear();
        product.getProductLangs().clear();
        product.getCategories().clear();

        for(Long id: dto.getShopIds()) {
            product.getCategories().add(new Category(id));
        }

        //product.getProductLangs().add(new ProductLang().title("Fuck").product(product).lang("ar"));
        int discount = 100 * (int)((dto.getPrice().doubleValue() - dto.getSalePrice().doubleValue())/dto.getPrice().doubleValue());

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


        //product.ref(ref).sku(sku).upc(upc).releaseDate(releaseDate);
        product = productRepository.save(product);
        return  addProductMapper.toDto(product);
    }

    public void addToElastic(Long id, String sku, String name, String name_ar, List<String> shops) {
        productSearchRepository.index(new AddProductDTO(id,
                sku,
                name,
                name_ar,
                shops));
    }

    public void importProducts(List<AddProductDTO> products, Long currentMerchantId, String currentMerchant, Long tenantId, List<Long> shopIds, String browseNode) {
        for(AddProductDTO doc: products) {
            Long id = doc.getId();
            doc.setId(null);
            String image = uploadToS3(doc.getImage(), currentMerchantId, currentMerchant, tenantId);
            doc.setImage(image);
            doc.setShopIds(shopIds);
            doc.setBrowseNode(browseNode);
            createMerchantProduct(doc, currentMerchantId, currentMerchant, tenantId );
            doc.setId(id);
            doc.setImported(true);
            productSearchRepository.save(doc);
        }
    }

    private String uploadToS3(String image, Long currentMerchantId, String currentMerchant, Long tenantId) {
        String t = TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        CRC32 checksum = new CRC32();
        checksum.update(image.getBytes());

        String objectKey = "_m/" + t + "/" + m + "/" + checksum.getValue()+image.substring(image.length()-4,image.length());


        try {
            //PutObjectRequest request = PutObjectRequest.builder().bucket(bucketName).key().contentType().build();
            BufferedImage img = ImageIO.read(new URL(image));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ImageIO.write(img,"png", outputStream);
            PutObjectResponse response = S3Util.getClient().putObject(PutObjectRequest.builder().bucket(S3Util.getBucketName()).key(objectKey).build(), RequestBody.fromBytes(outputStream.toByteArray()));
            return "https://cdn.badals.com/"+ objectKey.substring(3);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public MerchantProductResponse searchForTenant(String currentTenant, String text, Integer limit, Integer offset) {
        List<AddProductDTO> result = search(currentTenant + " AND imported:false AND "+text);
        MerchantProductResponse response = new MerchantProductResponse();
        response.setTotal(12);
        response.setHasMore((limit+offset) < 12);
        response.setItems(result);
        return response;
    }

//    public List<ProductDTO> listForMerchantsAll(Long merchantId) {
//        return productRepository.listForMerchantsAll(merchantId).stream().map(productMapper::toDto).collect(Collectors.toList());
//    }
}
