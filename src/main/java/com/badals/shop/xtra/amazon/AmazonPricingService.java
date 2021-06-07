package com.badals.shop.xtra.amazon;

import com.amazon.paapi5.v1.*;
import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.enumeration.Api;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Variation;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.CategoryRepository;
import com.badals.shop.repository.MerchantRepository;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.SlugService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.IProductService;
import com.badals.shop.xtra.PricingHelperService;
import com.badals.shop.xtra.amazon.mws.MwsItemNode;
import com.badals.shop.xtra.amazon.mws.MwsLookup;
import com.badals.shop.xtra.amazon.mws.MwsLookupParser;
import com.badals.shop.xtra.amazon.paapi5.PasLookup;
import com.badals.shop.xtra.amazon.paapi5.PasLookupParser;
import com.badals.shop.xtra.keepa.KeepaLookup;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Service
public class AmazonPricingService implements IProductService {

    private final Logger log = LoggerFactory.getLogger(AmazonPricingService.class);

    public static final Long AMAZON_US_MERCHANT_ID = 1L;

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepository;
    private final MerchantRepository merchantRepository;
    private final PricingHelperService helper;
    private final PasLookup pasLookup;
    private final MwsLookup mwsLookup;
    private final KeepaLookup keepaLookup;
    //private final RedisPasRepository redisPasRepository;
    private final ProductMapper productMapper;
    private final PasItemMapper pasItemMapper;
    private final PricingRequestService pricingRequestService;
    private final SlugService slugService;
    private final Pas5Service pas5Service;

    public AmazonPricingService(ProductRepository productRepo, CategoryRepository categoryRepository, MerchantRepository merchantRepository, PricingHelperService pricingHelperService, @Qualifier("us") PasLookup pasLookup, MwsLookup mwsLookup, KeepaLookup keepaLookup, /*RedisPasRepository redisPasRepository,*/ ProductMapper productMapper, PasItemMapper pasItemMapper, PricingRequestService pricingRequestService, SlugService slugService, Pas5Service pas5Service) {
        this.productRepo = productRepo;
        this.categoryRepository = categoryRepository;
        this.merchantRepository = merchantRepository;
        this.helper = pricingHelperService;
        this.pasLookup = pasLookup;
        this.mwsLookup = mwsLookup;
        this.keepaLookup = keepaLookup;
        //this.redisPasRepository = redisPasRepository;
        this.productMapper = productMapper;
        this.pasItemMapper = pasItemMapper;
        this.pricingRequestService = pricingRequestService;
        this.slugService = slugService;
        this.pas5Service = pas5Service;
    }


    @SneakyThrows
    @Transactional
    /**
     * Entry point
     */
    public Product lookup(String asin, boolean isRebuild) throws NoOfferException, PricingException {
        //return pas5Service.mwsItemShortCircuit(product, asin, false, 0);
        // Does Product Exist?
        Product product = productRepo.findBySkuJoinChildren(asin, AMAZON_US_MERCHANT_ID).orElse(new Product());

        if (product.getId() == null)
            return buildKeepa(product, asin, true);

        else if (product.getStub() != null && product.getStub())
            return buildKeepa(product, asin, false);

        else if (isRebuild) {
            return buildKeepa(product, asin, true);
        }

        else if (product.getExpires() != null && product.getExpires().isAfter(Instant.now()))
            return product;
        // check pas flag

        String parentAsin = null;
        if(product.getParent() != null)
            parentAsin = product.getParent().getSku();

        return helper.priceMws(product, helper.findOverrides(asin, parentAsin));
    }

    Product buildKeepa(Product product, String asin, Boolean isRating) throws PricingException, ProductNotFoundException, NoOfferException, IncorrectDimensionsException {
        PasItemNode item = null;
        /*if (redisPasRepository.getHashOperations().hasKey("keepa", asin)) {
            try {
                item = (PasItemNode) redisPasRepository.getHashOperations().get("keepa", asin);
            }
            catch (Exception e) {
                redisPasRepository.getHashOperations().delete("keepa", asin);
            }
        }
        else {*/
            try {
                item = keepaLookup.lookup(asin, isRating);
                //redisPasRepository.getHashOperations().put("keepa", asin, item);
                product.setPasFlag(true);
                product.setApi(Api.KEEPA);
            } catch (ItemNotAccessibleException e) {
                e.printStackTrace();
                product.setPasFlag(false);
                return pas5Service.mwsItemShortCircuit(product, asin, true, 0);
            }

        List<ProductOverride> overrides = helper.findOverrides(asin, item.getParentAsin());

        // Create & Price Product
        product = helper.initProduct(product, item, item.getVariationType() == VariationType.PARENT, overrides);
        product.setVariationType(item.getVariationType());

        if (item.getVariationType() != VariationType.PARENT) {
            product.setPricingApi(Api.KEEPA);
            if (product.getPrice() == null) {
                product.setPricingApi(Api.MWS);
                product = helper.priceMws(product, overrides);
            }
        }

        // If stub
        if (product.getStub())
            return productRepo.save(product);

        // Is part of variation? No
        if (item.getVariationType() == VariationType.SIMPLE) {
            product = productRepo.save(product);
            return product;
        }

        // Yes
        Product parent = null;
        if (item.getVariationType() == VariationType.CHILD) {
            helper.resetDescriptions(product);
            parent = productRepo.findBySkuJoinChildren(item.getParentAsin(), AMAZON_US_MERCHANT_ID).orElse(new Product());

            if (parent.getId() == null) {
                parent = helper.initProduct(parent, item, true, overrides);
                parent = helper.updateParentFromChildQuery(parent, item.getParentAsin(), AMAZON_US_MERCHANT_ID);
            }
        }
        else
            parent = product;

        // Save parent
        List<Product> existingChildren = null;
        if(parent.getId() == null) {
            parent = productRepo.saveAndFlush(parent);
            existingChildren = productRepo.findAllBySkuIsInAndMerchantId(item.getVariations().keySet(), AMAZON_US_MERCHANT_ID);

            //parent = productRepo.findBySkuJoinChildren(item.getParentAsin(), AMAZON_US_MERCHANT_ID).orElse(new Product());
        }

        Set<Product> children = parent.getChildren();
        List<Variation> variations = new ArrayList<Variation>();

        // Deactivate All Children
        children.stream().forEach(x -> x.setActive(false));

        // Existing children
        for (String childAsin : item.getVariations().keySet()) {
            List<Attribute> value = item.getVariations().get(childAsin);
            Product child = null;
            if(asin.equals(childAsin))
                child = product.variationAttributes(value);

            if (child == null)
                child = children.stream().filter(x -> x.getSku().equals(childAsin)).findFirst().orElse(null);

            if (child == null && existingChildren != null)
                child = existingChildren.stream().filter(x -> x.getSku().equals(childAsin)).findFirst().orElse(null);

            if (child == null)
                child = helper.initStub(childAsin, value, AMAZON_US_MERCHANT_ID);

            if(child.getId() ==  null || child.getParentId() != parent.getRef()) {
                child.setParent(parent);
                child.setParentId(parent.getRef());
                children.add(child);
            }

            child.setActive(true);
            variations.add(new Variation(child.getRef(), value));
        }

        item = null;
        parent.setChildren(children);
        parent.setVariations(variations);
        parent = productRepo.saveAndFlush(parent);

        return parent.getChildren().stream().filter(x -> x.getSku().equals(asin)).findFirst().orElse(parent);
    }



    Product pricePas(Product product, String asin) {
        List<String> list = new ArrayList();
        list.add(asin);

        GetItemsResponse response = null;
        response = pasLookup.lookup(list);

        if(response.getErrors() != null && response.getErrors().size() > 0 ) {
            ErrorData error = response.getErrors().get(0);
            if (error.getCode().trim().equalsIgnoreCase("ItemNotAccessible")) {
                //response = mwsLookup.lookup(asin);
                //product.setFlag(true);
                //return priceMws(product);
            }
        }

        Map<String, Item> doc = parse_response(response.getItemsResult().getItems());
        PasItemNode item = pasItemMapper.itemToPasItemNode(doc.get(asin));

        List<ProductOverride> overrides = pricingRequestService.findOverrides(item.getId(), item.getParentAsin());
        product = helper.initProduct(product, item, false, overrides);
        product = productRepo.save(product);

        if( product.getPrice() == null) {
            //product.setPrimeFlag(false);
            //return priceMws(product);
        }

        product = productRepo.save(product);
        return product;
    }

    private PasItemNode callPas(String asin) throws PricingException {
        GetItemsResponse response = null;
        List<String> list = new ArrayList();
        list.add(asin);
        Map<String, Item> doc;
        response = pasLookup.lookup(list);

        //ErrorData error = response.getErrors().get(0);
        //if (error.getCode().trim().equalsIgnoreCase("ItemNotAccessible")) {

        if (response.getErrors() != null && response.getErrors().size() > 0) {
            throw new PricingException("Unable to price this item!");
        }

        doc = parse_response(response.getItemsResult().getItems());
        return pasItemMapper.itemToPasItemNode(doc.get(asin));
    }


    private Product priceMws(Product p, List<ProductOverride> overrides) throws NoOfferException {

        if (p.getWeight() == null || p.getWeight().doubleValue() < PasUtility.MINWEIGHT) return p;
        MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;
        try {
            product = helper.setMerchantStock(p, MwsLookupParser.parseStock(helper.getMerchantStock(p),n, p.getWeight(), overrides), BigDecimal.valueOf(99L));
            product.inStock(true);
        } catch (PricingException e) {
            product.setPrice((BigDecimal) null);
            //e.printStackTrace();
        } catch (NoOfferException e) {
            //product = setMerchantStock(p, getMerchantStock(p),BigDecimal.ZERO);
            product.inStock(false);
        }
        return product;
    }

    private Product pricePas(Product p, PasItemNode item, List<ProductOverride> overrides) throws NoOfferException {

        if (p.getWeight() == null || p.getWeight().doubleValue() < PasUtility.MINWEIGHT) return p;
        //MwsItemNode n = mwsLookup.fetch(p.getSku());
        Product product = p;

        MerchantStock stock = helper.getMerchantStock(product);
        try {
            product = helper.setMerchantStock(product, PasLookupParser.parseStock(product, stock, item, overrides),BigDecimal.valueOf(99L));
            product.inStock(true);
        } catch (PricingException e) {
            product.setPrice((BigDecimal) null);
            //e.printStackTrace();//@Todo set stock quantity to 0
        } catch (NoOfferException e) {
            //e.printStackTrace();
            product.inStock(false);
        }
        return product;
    }



    private static Map<String, Item> parse_response(List<Item> items) {
        Map<String, Item> mappedResponse = new HashMap<String, Item>();
        for (Item item : items) {
            mappedResponse.put(item.getASIN(), item);
        }
        return mappedResponse;
    }


    public ProductResponse searchItems(String keyword) throws NoOfferException {
        SearchItemsResponse search = pasLookup.searchItems(keyword);
        if (search.getErrors() != null)
            throw new NoOfferException("No results found");

        ProductResponse response = new ProductResponse();
        List<ProductDTO> dtos = new ArrayList<ProductDTO>();

        for(Item x : search.getSearchResult().getItems()) {
            PasItemNode item = pasItemMapper.itemToPasItemNode(x);
            Product product = helper.initProduct(new Product(), item, false, null);
            dtos.add(productMapper.toDto(product));
        }

        response.setItems(dtos);
        response.setTotal(dtos.size());
        response.setHasMore(false);

        return response;
    }

}
