package com.badals.shop.service;

import com.algolia.search.SearchIndex;
import com.badals.shop.domain.AlgoliaProduct;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.graph.MerchantProductResponse;
import com.badals.shop.graph.ProductResponse;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.search.ProductSearchRepository;
import com.badals.shop.service.dto.TenantDTO;
import com.badals.shop.service.mapper.AddProductMapper;
import com.badals.shop.service.mapper.AlgoliaProductMapper;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class ProductIndexService {

    private final Logger log = LoggerFactory.getLogger(ProductIndexService.class);
    private final ProductSearchRepository productSearchRepository;
    private final ProductRepository productRepository;
    private final AddProductMapper addProductMapper;
    private final TenantService tenantService;
    private final ProductContentService productContentService;
    private final AlgoliaProductMapper algoliaProductMapper;
    private final SearchIndex<AlgoliaProduct> index;
    private final Pas5Service pas5Service;


    public ProductIndexService(ProductSearchRepository productSearchRepository, ProductRepository productRepository, AddProductMapper addProductMapper, TenantService tenantService, ProductContentService productContentService, AlgoliaProductMapper algoliaProductMapper, SearchIndex<AlgoliaProduct> index, Pas5Service pas5Service) {
        this.productSearchRepository = productSearchRepository;
        this.productRepository = productRepository;
        this.addProductMapper = addProductMapper;
        this.tenantService = tenantService;
        this.productContentService = productContentService;
        this.algoliaProductMapper = algoliaProductMapper;
        this.index = index;
        this.pas5Service = pas5Service;
    }
/*    private final TenantService tenantService;
    private final SpeedDialService speedDialService;*/



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

    public void addToElastic(Long id, String sku, String name, String name_ar, List<String> shops) {
        productSearchRepository.index(new AddProductDTO(id,
                sku,
                name,
                name_ar,
                shops));
    }

    public Attribute indexProduct(long id) {
        Product product = productRepository.getOne(id);
        // AlgoliaProduct algoliaProduct = algoliaProductMapper.producttoAlgoliaProduct(product);

        /*for(ProductLang i: productLangRepository.findAllByProductId( id ) ){
            algoliaProduct.getI18().put(i.getLang(), new ProductI18(i.getTitle(), "", ""));
        }*/
        // index.saveObject(algoliaProduct);
        return new Attribute("success", "1");
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
        //List<AddProductDTO> result = search(hashtag );
        List<AddProductDTO> result = productSearchRepository.findByHashtagsContains(hashtag);
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
        saveToElastic(p);
    }

    protected void saveToElastic(Product product) {
        AddProductDTO dto = addProductMapper.toDto(product);
        saveToElastic(dto);
    }

    protected void saveToElastic(AddProductDTO dto) {
        dto.setId(dto.getRef());
        productSearchRepository.save(dto);
    }

    public void importProducts(List<AddProductDTO> products, Long currentMerchantId, String currentMerchant, Long tenantId, String currentTenant, List<Long> shopIds, String browseNode) {
        TenantDTO tenantObj = tenantService.findOne(tenantId).get();

        for(AddProductDTO doc: products) {
            Long id = doc.getId();
            doc.setId(null);
            if (doc.getImage() != null ) {
                String image = productContentService.uploadToS3(doc.getImage(), currentMerchantId, currentMerchant, tenantId);
                doc.setImage(image);
            }
            if(!doc.getSku().startsWith(tenantObj.getSkuPrefix()))
                doc.setSku(tenantObj.getSkuPrefix()+doc.getSku());

            doc.setId(id);
            doc.setImported(true);
            doc.setMerchant(currentMerchant);
            doc.setTenant(currentTenant);
            AlgoliaProduct algoliaProduct = algoliaProductMapper.addProductToAlgoliaProduct(doc);

            saveToElastic(doc);
            index.saveObject(algoliaProduct);

        }
    }

    public ProductResponse findFromPas(String keyword) throws NoOfferException {
        return pas5Service.searchItems(keyword);
    }
}
