package com.badals.shop.service;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.domain.tenant.S3UploadRequest;
import com.badals.shop.domain.tenant.TenantProduct;
import com.badals.shop.domain.tenant.TenantStock;
import com.badals.shop.graph.PartnerProductResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.S3UploadRequestRepository;
import com.badals.shop.repository.TenantHashtagRepository;
import com.badals.shop.repository.TenantProductRepository;
import com.badals.shop.repository.search.PartnerProductSearchRepository;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import com.badals.shop.service.mapper.*;
import com.badals.shop.service.pojo.ChildProduct;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.util.ChecksumUtil;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class TenantAdminProductService {

    private final Logger log = LoggerFactory.getLogger(TenantAdminProductService.class);
    private final TenantProductRepository productRepository;
    private final MessageSource messageSource;
    private final AddProductMapper addProductMapper;
    private final PartnerProductMapper partnerProductMapper;
    private final ChildProductMapper childProductMapper;

    private final TenantHashtagRepository hashtagRepository;
    private final TenantHashtagMapper tenantHashtagMapper;
    private final TenantProductMapper productMapper;

    private final ProductLangMapper productLangMapper;
    private final PartnerProductSearchRepository partnerProductSearchRepository;

    private final TenantService tenantService;
    private final RecycleService recycleService;
    private final SlugService slugService;
    private final ProductIndexService productIndexService;

    private final ProductService productService;
    private final S3UploadRequestRepository s3UploadRequestRepository;


    public TenantAdminProductService(TenantProductRepository productRepository, MessageSource messageSource, ProductMapper productMapper, AddProductMapper addProductMapper, PartnerProductMapper partnerProductMapper, ChildProductMapper childProductMapper, TenantHashtagRepository hashtagRepository, TenantHashtagMapper tenantHashtagMapper, TenantProductMapper tenantProductMapper, ProductLangMapper productLangMapper, PartnerProductSearchRepository partnerProductSearchRepository, TenantService tenantService, RecycleService recycleService, SlugService slugService, ProductIndexService productIndexService, ProductService productService, S3UploadRequestRepository s3UploadRequestRepository) {
        this.productRepository = productRepository;
        this.messageSource = messageSource;
        this.productMapper = tenantProductMapper;
        this.addProductMapper = addProductMapper;
        this.partnerProductMapper = partnerProductMapper;
        this.childProductMapper = childProductMapper;
        this.hashtagRepository = hashtagRepository;
        this.tenantHashtagMapper = tenantHashtagMapper;
        this.productLangMapper = productLangMapper;
        this.partnerProductSearchRepository = partnerProductSearchRepository;
        this.tenantService = tenantService;
        this.recycleService = recycleService;
        this.slugService = slugService;
        this.productIndexService = productIndexService;
        this.productService = productService;
        this.s3UploadRequestRepository = s3UploadRequestRepository;
    }

    public ProductResponse adminSearchTenantProducts(String upc, String title) {
        //PartnerProduct p = partnerProductSearchRepository.findBySlug(upc);

        //log.info(p.getSlug());

        List <PartnerProduct> products = partnerProductSearchRepository.findByTenantIdEqualsAndUpcEquals(TenantContext.getCurrentTenant(), upc);
        //List <PartnerProduct> products = partnerProductSearchRepository.findByTenantEquals(TenantContext.getCurrentTenant());
        Integer total = products.size();
        ProductResponse response = new ProductResponse();
        response.setTotal(total);
        response.setHasMore(false);
        response.setItems(products.stream().map(partnerProductMapper::toProductDto).collect(Collectors.toList()));
        return response;
    }

    public PartnerProduct getPartnerProduct(String id) {
        TenantProduct product = productRepository.findByRefJoinChildren(id).get();
        return partnerProductMapper.toDto(product);
    }

    public void sanityCheck(PartnerProduct dto) {
        if(dto.getSku() == null && dto.getId() != null) {
            throw new ValidationException("Sku required for new products");
        }
    }

    @Transactional
    public void logUploadRequest(String key, String url) {
        S3UploadRequest request = new S3UploadRequest();
        request.setKey(key);
        request.setUrl(url);
        s3UploadRequestRepository.save(request);
    }

    public PartnerProduct savePartnerProduct(PartnerProduct dto, boolean isSaveES) throws ProductNotFoundException, ValidationException {
        Long currentMerchantId = TenantContext.getCurrentMerchantId();
        String currentTenantId = TenantContext.getCurrentTenant();
        TenantProduct update = partnerProductMapper.toEntity(dto);
        final TenantProduct master;
        boolean _new = dto.getId() == null;

        if(!_new)
            master = productRepository.findByIdJoinChildren(dto.getId()).orElseThrow( () ->  new ProductNotFoundException("No Product found for ID"));
        else {
            master = partnerProductMapper.toEntity(dto);
        }

        if(_new) {

            Long ref = slugService.generateRef(dto.getSku(), currentMerchantId);
            master.setRef(String.valueOf(ref));
            master.setSlug(String.valueOf(ref));
/*            if(master.getProductLangs() != null)
                master.getProductLangs().stream().forEach(x -> x.setProduct(master));*/
            master.setMerchantId(currentMerchantId);
            master.setTenantId(currentTenantId);
        }
        else {
            if(master.getSku().equals(update.getSku())) {
                //String ref = currentMerchantId.toString() + ChecksumUtil.getChecksum(dto.getSku());
                master.setSku(update.getSku());
                //master.setRef(Long.valueOf(ref));
                //master.setSlug(ref);
            }
            master.setTitle(update.getTitle());
            master.setLangs(update.getLangs());
            master.setWeight(update.getWeight());
            master.setPrice(update.getPrice());
            master.setListPrice(update.getPrice());
            master.setImage(update.getImage());
            master.setUpc(update.getUpc());
            master.setHashtags(update.getHashtags());
            master.setBrand(update.getBrand());
            master.setUnit(update.getUnit());
            master.setGallery(update.getGallery());
            master.setVariationOptions(update.getVariationOptions());

            //saveLang(master, update);
        }

        if(update.getChildren() == null || update.getVariationType().equals(VariationType.SIMPLE)) {
            assert(dto.getPrice() != null);
            master.setVariationType(VariationType.SIMPLE);
            TenantStock stock  = null;
            if(master.getStock() != null) {
                stock = master.getStock().stream().findFirst().orElse(new TenantStock());
                stock = setStock(stock, master,/* dto.getPriceObj(), dto.getSalePriceObj()==null?dto.getPriceObj():dto.getSalePriceObj(),*/ dto.getCost(), dto.getQuantity(), dto.getAvailability(), currentMerchantId);
            }
            if(stock != null && stock.getId() == null)
                master.addStock(stock);
        }
        else { // PARENT    //if(product.getVariationType().equals(VariationType.PARENT)) {
            assert(dto.getPrice() == null);
            master.setVariationType(VariationType.PARENT);
            saveChildren(master, update, dto, currentMerchantId, _new);
        }




        master.setActive(false);
        master.setMerchantId(currentMerchantId);
        productRepository.save(master);

        PartnerProduct esDto = partnerProductMapper.toDto(master);
        if(isSaveES)
            partnerProductSearchRepository.save(esDto);
        return partnerProductMapper.toDto(master);
    }

    private void saveChildren(TenantProduct master, TenantProduct update, PartnerProduct dto, Long currentMerchantId, boolean _new) {
        Set<TenantProduct> masterChildren = master.getChildren();
        Set<TenantProduct> updateChildren = update.getChildren();


        if(_new) {
            if(masterChildren != null)
                masterChildren.stream().forEach(x -> x.variationType(VariationType.CHILD).active(true).slug(String.valueOf(slugService.generateRef(x.getSku(), currentMerchantId))).merchantId(currentMerchantId).ref(x.getSlug()).title(master.getTitle()).parent(master));
            return;
        }
        // Delete removed
        List<TenantProduct> remove = new ArrayList<TenantProduct>();
        for (TenantProduct c : masterChildren) {
            //product.getChildren().stream().forEach(x -> x.variationType(VariationType.SIMPLE).active(true).ref(Long.parseLong(product.getRef().toString() + i++)).setParent(product));
            TenantProduct pl = updateChildren.stream().filter(x -> x.getId() != null && x.getId().equals(c.getId())).findFirst().orElse(null);
            if (pl == null) {
                recyleImages(master, c);
                remove.add(c);
            }
        }
        masterChildren.removeAll(remove);

        for (TenantProduct c: updateChildren) {

            if (c == null || c.getId() == null) {
                String ref = currentMerchantId.toString() + String.valueOf(ChecksumUtil.getChecksum(c.getSku()));
                c.setRef(ref);
                c.setSlug(ref);
                c.setActive(true);
                c.setTitle(generateTitle(master.getTitle(), c.getVariationAttributes()));
                c.setMerchantId(currentMerchantId);
                //c.getStock().stream().findFirst().get().setMerchantId(currentMerchantId);
                master.addChild(c);
                continue;
            }
            TenantProduct pl = masterChildren.stream().filter(x -> x.getId().equals(c.getId())).findFirst().orElse(null);
            ChildProduct dto2 = dto.getChildren().stream().filter(x -> x.getId().equals(c.getId())).findFirst().orElse(null);
/*            if(!dto2.isDirty)
                continue;*/
            //String ref = currentMerchantId.toString() + String.valueOf(ChecksumUtil.getChecksum(c.getSku()));
            //pl.setRef(Long.valueOf(ref));
            ///pl.setSlug(ref);
            pl.setVariationAttributes(c.getVariationAttributes());
            pl.setTitle(generateTitle(master.getTitle(), c.getVariationAttributes()));
            pl.setImage(c.getImage());
            pl.setGallery(c.getGallery());
            pl.sku(c.getSku()).image(c.getImage()).upc(c.getUpc()).weight(c.getWeight()).gallery(c.getGallery());
            TenantStock stock = pl.getStock().stream().findFirst().orElse(new TenantStock());
            stock = setStock(stock, master, /*dto2.getPriceObj(), dto2.getSalePriceObj(),*/ dto2.getCost(), dto2.getQuantity(), dto2.getAvailability(), currentMerchantId);
            if(stock.getId() == null)
                pl.addStock(stock);

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

    @Transactional
    public ProductResponse findPartnerProducts(String text, Integer limit, Integer offset, Boolean active) {
        //List<AddProductDTO> result = search("tenant:"+currentTenant + " AND imported:" + imported.toString() + ((text != null)?" AND "+text:""));
        String profile = TenantContext.getCurrentProfile();
        String like = null;
        if(text != null)
            like = "%"+text+"%";

        Integer total = productRepository.countForTenant(like, VariationType.CHILD);
        List<TenantProduct> result = productRepository.listForTenantAll(like, VariationType.CHILD, PageRequest.of((int) offset / limit, limit));
        ProductResponse response = new ProductResponse();
        response.setTotal(total);
        response.setHasMore((limit + offset) < total);
        response.setItems(result.stream().map(productMapper::toPartnerListDto).collect(Collectors.toList()));
        return response;
    }

    public void deleteProduct(Long id) throws ProductNotFoundException {
        final TenantProduct product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found in the database"));
        verifyOwnership(product);
        productRepository.delete(true, id);
    }

    @Transactional
   public void setProductPublished(Long id, Boolean value) throws ProductNotFoundException {
       final TenantProduct product = productRepository.findOneByRef(id.toString()).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found in the database"));
       verifyOwnership(product);

       product.setActive(value);
       if(product.getChildren() != null) {
           product.getChildren().stream().forEach(x -> x.setActive(value));
       }
       productRepository.save(product);
   }

    private void verifyOwnership(TenantProduct product) throws ProductNotFoundException {
        String tenant = TenantContext.getCurrentProfile();
        if(!product.getTenantId().equals(tenant))
            throw new ProductNotFoundException("Product not available");
    }


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

    public boolean exists(String productId) {
        if (productRepository.findOneByRef(productId).isPresent())
            return true;

        return false;
    }
}
