package com.badals.shop.service;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.search.ProductSearchRepository;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.service.mapper.AddProductMapper;
import com.badals.shop.service.mapper.PartnerProductMapper;
import com.badals.shop.service.mapper.ProductMapper;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.ChildProduct;
import com.badals.shop.service.pojo.PartnerProduct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.zip.CRC32;

/**
 * Service Implementation for managing {@link Product}.
 */
@Service
@Transactional
public class PartnerService {

    private final Logger log = LoggerFactory.getLogger(PartnerService.class);
    private final ProductRepository productRepository;
    private final ProductService productService;
    private final MessageSource messageSource;
    private final ProductMapper productMapper;
    private final AddProductMapper addProductMapper;
    private final PartnerProductMapper partnerProductMapper;
    private final ProductSearchRepository productSearchRepository;
    private final TenantService tenantService;

    public PartnerService(ProductRepository productRepository, ProductService productService, MessageSource messageSource, ProductMapper productMapper, AddProductMapper addProductMapper, PartnerProductMapper partnerProductMapper, ProductSearchRepository productSearchRepository, TenantService tenantService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.messageSource = messageSource;
        this.productMapper = productMapper;
        this.addProductMapper = addProductMapper;
        this.partnerProductMapper = partnerProductMapper;
        this.productSearchRepository = productSearchRepository;
        this.tenantService = tenantService;
    }

    public PartnerProduct getPartnerProduct(Long id, Long merchantId) {
        Product product = productRepository.findByIdJoinChildren(id, merchantId).get();
        return partnerProductMapper.toDto(product);
    }

    public AddProductDTO createPartnerProduct(PartnerProduct dto, Long currentMerchantId, String currentMerchant, Long merchantId, String currentTenant, boolean isSaveES) {
        final Product product;
        if(dto.getId() != null)
            product = productRepository.findById(dto.getId()).get();
        else if(dto.getRef() != null)
            product = productRepository.findById(dto.getId()).get();
        else
            product = partnerProductMapper.toEntity(dto);

        CRC32 checksum = new CRC32();
        if(product.getId() == null) {
            product.setSku(dto.getSku());
            checksum.update(dto.getSku().getBytes());
            String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());
            product.setRef(Long.valueOf(ref));
            product.setSlug(ref);
        }
        else if(dto.getRef() == null || dto.getRef().equals("")) {
            String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());
            product.setRef(Long.valueOf(ref));
            product.setSlug(ref);
        }

        if(product.getVariationType() == null) {
            product.setVariationType(VariationType.SIMPLE);
            int discount = 100 * (int)((dto.getPrice().doubleValue() - dto.getSalePrice().doubleValue())/dto.getPrice().doubleValue());

            product.getMerchantStock().add(new MerchantStock().quantity(dto.getQuantity()).availability(dto.getAvailability()).cost(dto.getCost()).allow_backorder(false)
                    .price(dto.getSalePrice()).discount(discount).product(product).merchantId(currentMerchantId));
        }
        else if(product.getVariationType().equals(VariationType.PARENT)) {
            //final Integer i = 1;
            //product.getChildren().stream().forEach(x -> x.variationType(VariationType.SIMPLE).active(true).ref(Long.parseLong(product.getRef().toString() + i++)).setParent(product));
            for (Product c : product.getChildren()) {
                c.setVariationType(VariationType.CHILD);
                c.setActive(true);
                c.setStub(false);
                checksum.update(c.getSku().getBytes());
                String ref = currentMerchantId.toString() + String.valueOf(checksum.getValue());

                c.setRef(Long.valueOf(ref));
                c.setSlug(ref);
                c.setParent(product);
                c.getMerchantStock().forEach(x -> x.product(c).merchantId(currentMerchantId));
            }
        }
        product.getProductLangs().stream().forEach(x -> x.setProduct(product));
        product.setActive(false);

        product.setMerchantId(merchantId);

        productRepository.save(product);

        AddProductDTO esDto = addProductMapper.toDto(product);
        if(isSaveES)
            productService.saveToElastic(esDto);
        return  esDto;
    }

    public void savePrice(Long id, Price price) {
    }

    public void saveLang(Long id, ProductLangDTO lang) {
    }

    public void saveChild(Long id, ChildProduct child) {
    }
}
