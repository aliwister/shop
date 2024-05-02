package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.*;
import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.TenantHashtagRepository;
import com.badals.shop.repository.TenantProductRepository;
import com.badals.shop.repository.TenantRepository;
import com.badals.shop.service.dto.MerchantDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import com.badals.shop.service.mapper.*;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing {@link TenantProduct}.
 */
@Service
@Transactional
public class TenantProductService {

    private final Logger log = LoggerFactory.getLogger(TenantProductService.class);
    private final TenantProductRepository productRepository;
    private final MessageSource messageSource;
    private final PartnerProductMapper partnerProductMapper;
    private final ChildProductMapper childProductMapper;

    private final TenantHashtagRepository hashtagRepository;
    private final TenantHashtagMapper tenantHashtagMapper;
    private final TenantProductMapper productMapper;

    private final TenantService tenantService;
    private final RecycleService recycleService;
    private final SlugService slugService;
    private final MerchantService merchantService;
    private final TenantRepository tenantRepository;

    public TenantProductService(TenantProductRepository productRepository, MessageSource messageSource, PartnerProductMapper partnerProductMapper, ChildProductMapper childProductMapper, TenantHashtagRepository hashtagRepository, TenantHashtagMapper tenantHashtagMapper, TenantProductMapper productMapper, TenantService tenantService, RecycleService recycleService, SlugService slugService, MerchantService merchantService, TenantRepository tenantRepository) {
        this.productRepository = productRepository;
        this.messageSource = messageSource;
        this.partnerProductMapper = partnerProductMapper;
        this.childProductMapper = childProductMapper;
        this.hashtagRepository = hashtagRepository;
        this.tenantHashtagMapper = tenantHashtagMapper;
        this.productMapper = productMapper;
        this.tenantService = tenantService;
        this.recycleService = recycleService;
        this.slugService = slugService;
        this.merchantService = merchantService;
        this.tenantRepository = tenantRepository;
    }

    @Transactional
    public TenantProduct save(TenantProduct product) {
        log.debug("Request to save TenantProduct : {}", product);
        TenantProduct result = productRepository.save(product);
        return productRepository.save(result);
    }

    @Transactional
    public ProductResponse getPartnerProducts(Integer limit, Integer offset){
        Page<TenantProduct> result = productRepository.findAllByVariationTypeIsNotAndNotDeleted(VariationType.CHILD, PageRequest.of((int) offset/limit,limit));  //listForTenantAll(like, VariationType.CHILD, PageRequest.of((int) offset / limit, limit));
        ProductResponse response = new ProductResponse();

        response.setHasMore(response.isHasMore());
        response.setItems(result.stream().map(productMapper::toDto).collect(Collectors.toList()));
        return response;
    }
    public PartnerProduct getPartnerProduct(Long id) {
        TenantProduct product = productRepository.findByIdJoinChildren(id).get();
        return partnerProductMapper.toDto(product);
    }

    public TenantProduct getPartnerProductByRef(String ref) throws ProductNotFoundException {
        return productRepository.findByRefJoinChildren(ref).orElseThrow(()->new ProductNotFoundException("profuct not found by ref"));
    }

    public void sanityCheck(PartnerProduct dto) {
        if(dto.getSku() == null && dto.getId() != null) {
            throw new ValidationException("Sku required for new products");
        }
    }

    private String generateTitle(String title, List<Attribute> variationAttributes) {
        return title + variationAttributes.stream().map(Attribute::getValue).collect(Collectors.joining(", "));
    }


    public void deleteImage(Long id, String image) throws ProductNotFoundException {
        final TenantProduct product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found in the database"));
        List<Gallery> gallery = product.getGallery();
        gallery.remove(new Gallery(image));
        productRepository.saveAndFlush(product);

        recycleService.recycleS3("s3", image);
    }

    public void recyleImages(TenantProduct parent, TenantProduct child) {
        List<Gallery> gallery = parent.getGallery();
        if (gallery.indexOf(new Gallery(child.getImage())) < 0) {
            recycleService.recycleS3("s3", child.getImage());
        }

        for (Gallery img: child.getGallery()) {
            if (gallery.indexOf(img) < 0) {
                recycleService.recycleS3("s3", img.getUrl());
            }
        }
    }


    private TenantStock setStock(TenantStock stock, TenantProduct master, Price costPriceObj, BigDecimal quantity, Integer availability, Long currentMerchantId) {
/*
        if(priceObj == null)
            throw new ValidationException("Price is null");


        BigDecimal price = priceObj.getAmount();
        String currency = priceObj.getCurrency();

        if(price == null || currency == null)
            throw new ValidationException("Price or currency is null");

        int discount = 0;
        if(salePriceObj != null && salePriceObj.getAmount() != null ) {
            double salePrice = salePriceObj.getAmount().doubleValue();
            discount = 100 * (int)((price.doubleValue() - salePrice)/price.doubleValue());
            price = salePriceObj.getAmount();
        }
*/

        if(quantity == null)
            quantity = BigDecimal.ZERO;

        return stock.quantity(quantity).availability(availability).cost(costPriceObj).allow_backorder(false)
                /*.price(salePriceObj)*/.product(master);
    }


    public void deleteProduct(Long id) throws ProductNotFoundException {
        final TenantProduct product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found in the database"));
        verifyOwnership(product);
        productRepository.delete(true, id);
    }

    private void verifyOwnership(TenantProduct product) throws ProductNotFoundException {
        String tenant = TenantContext.getCurrentProfile();
        if(!product.getTenantId().equals(tenant))
            throw new ProductNotFoundException("Product not available");
    }

    @Cacheable("tags-products")
    public ProductResponse findByHashtag(String tag) {
        String profile = TenantContext.getCurrentProfile();
        List<ProductDTO> products = productRepository.findActiveTagProductsForTenant(tag, profile).stream().map(productMapper::toDto).collect(Collectors.toList());
        ProductResponse response = new ProductResponse();
        response.setItems(products);
        response.setTotal(products.size());
        response.setHasMore(false);
        return response;
    }
    public List<ProfileHashtagDTO> tenantTags() {
        String profile = TenantContext.getCurrentProfile();
        return hashtagRepository.findForList(profile).stream().map(tenantHashtagMapper::toDto).collect(Collectors.toList());
    }

    public ProductDTO findProductBySlug(String slug) throws ProductNotFoundException {
        //String profile = TenantContext.getCurrentProfile();
        TenantProduct product = productRepository.findBySlug(slug).get();

        if(product == null)
            throw new ProductNotFoundException("Invalid Product");

        if(product.getVariationType().equals(VariationType.PARENT)) {
            if(product.getChildren().size() <1)
                throw new ProductNotFoundException("Lonely Parent");
            product = product.getChildren().stream().filter(p -> p.getStub() == false).findFirst().orElse(product.getChildren().stream().findFirst().get());
        }
        if(product.getStub() != null && product.getStub()) {
            //Product p = lookup(product.getSku());
            return productMapper.toDto(product);
        }

        if(product.getExpires() != null && product.getExpires().isBefore(Instant.now())) {
            //Product p = lookup(product.getSku());
            product.setStub(true);
            return productMapper.toDto(product);
            //return this.getProductBySku(p, product.getSku());
        }
/*        else if (product.getExpires() == null && product.getUpdated().isBefore(Instant.now().minusSeconds(DEFAULT_WINDOW))) {
            product.setStub(true);
            return productMapper.toDto(product);
            //Product p = lookup(product.getSku());
            //return this.getProductBySku(p, product.getSku());
        }*/

        return productRepository.findBySlug(slug).map(productMapper::toDto).orElse(null);
    }

    public boolean exists(String productRef) {
        if (productRepository.findOneByRef(productRef).isPresent())
            return true;

        return false;
    }

    public ProductDTO createStubFromSearch(ProductDTO dto, String tag) throws URISyntaxException {

        Long merchantId = 1L;

        if(dto.getUrl() != null) {
            String domain = getDomainName(dto.getUrl());
            MerchantDTO merchant = merchantService.merchantByDomain(domain);
            if(merchant != null)
                merchantId = merchant.getId();
        }
        TenantProduct product = productRepository.findOneBySkuAndMerchantId(dto.getSku(), merchantId).orElse(productMapper.toEntity(dto));
        if(product.getId() != null) {
            TenantProduct update = productMapper.toEntity(dto);
            product.setRating(dto.getRating());
            product.setPrice(update.getPrice());
            product.setListPrice(update.getPrice());
            product.setImage(update.getImage());
            product.setWeight(update.getWeight());
            product.setLangs(update.getLangs());
            product.setVariationType(update.getVariationType());
            product.setApi(update.getApi());
            product.setUrl(update.getUrl());

            TenantStock stock = product.getStock().stream().findFirst().orElse(null);
            if (stock == null)
                stock = new TenantStock();
            stock.setCost(new Price(dto.getCost(), "USD"));
            stock.setQuantity(BigDecimal.valueOf(49));
            if (stock.getId() == null)
                product.addStock(stock.allow_backorder(true).availability(200));
        }
        if(product.getId() == null) {
            product = initSearchStub(product, merchantId);
            TenantStock stock = new TenantStock();
            stock.setCost(new Price(dto.getCost(), "USD"));
            stock.setQuantity(BigDecimal.valueOf(49));
            product.addStock(stock.allow_backorder(true).availability(200));
        }

        if(tag != null) {
            product.addTag(tag);
        }
        product =  productRepository.save(product);
        //dto.setRef(product.getRef());
        dto.setId(Long.valueOf(product.getRef()));
        dto.setRef(Long.valueOf(product.getRef()));
        //dto.setRef(product.getRef());

        return dto;
    }

    public TenantProduct initSearchStub(TenantProduct p, Long merchantId) {
        //p.setVariationType(VariationType.SEARCH);
        Long ref = slugService.generateRef(p.getSku(), merchantId);
        p.slug(String.valueOf(ref)).ref(String.valueOf(ref)).merchantId(merchantId).active(true).stub(true);
        p.setMerchantId(merchantId);
        return p;
    }

    public ProductDTO removeTag(String ref, String tag) {
        TenantProduct product = productRepository.findOneByRef(ref).orElse(null);
        if (product == null)
            return null;
        product.removeTag(tag);
        product = productRepository.save(product);
        return productMapper.toDto(product);
    }
    public static String getDomainName(String url) throws URISyntaxException {
        URI uri = new URI(url);
        String domain = uri.getHost();
        return domain.startsWith("www.") ? domain.substring(4) : domain;
    }
}
