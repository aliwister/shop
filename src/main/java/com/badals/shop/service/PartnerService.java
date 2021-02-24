package com.badals.shop.service;

import com.badals.shop.domain.MerchantStock;
import com.badals.shop.domain.Product;
import com.badals.shop.domain.ProductLang;
import com.badals.shop.domain.enumeration.VariationType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.domain.pojo.Gallery;
import com.badals.shop.domain.pojo.MerchantProductResponse;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.repository.ProductRepository;
import com.badals.shop.repository.search.ProductSearchRepository;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.service.mapper.*;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.ChildProduct;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.util.ChecksumUtil;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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
    private final ChildProductMapper childProductMapper;

    private final ProductLangMapper productLangMapper;
    private final ProductSearchRepository productSearchRepository;
    private final TenantService tenantService;
    private final RecycleService recycleService;

    public PartnerService(ProductRepository productRepository, ProductService productService, MessageSource messageSource, ProductMapper productMapper, AddProductMapper addProductMapper, PartnerProductMapper partnerProductMapper, ChildProductMapper childProductMapper, ProductLangMapper productLangMapper, ProductSearchRepository productSearchRepository, TenantService tenantService, RecycleService recycleService) {
        this.productRepository = productRepository;
        this.productService = productService;
        this.messageSource = messageSource;
        this.productMapper = productMapper;
        this.addProductMapper = addProductMapper;
        this.partnerProductMapper = partnerProductMapper;
        this.childProductMapper = childProductMapper;
        this.productLangMapper = productLangMapper;
        this.productSearchRepository = productSearchRepository;
        this.tenantService = tenantService;
        this.recycleService = recycleService;
    }

    public PartnerProduct getPartnerProduct(Long id, Long merchantId) {
        Product product = productRepository.findByIdJoinChildren(id, merchantId).get();
        return partnerProductMapper.toDto(product);
    }

    public PartnerProduct savePartnerProduct(PartnerProduct dto, Long currentMerchantId, boolean isSaveES) throws ProductNotFoundException {
        Product update = partnerProductMapper.toEntity(dto);
        final Product master;
        boolean _new = dto.getId() == null;

        if(!_new)
            master = productRepository.findByIdJoinChildren(dto.getId(), currentMerchantId).orElseThrow( () ->  new ProductNotFoundException("No Product found for ID"));
        else
            master = partnerProductMapper.toEntity(dto);

        if(_new) {
            String ref = currentMerchantId.toString() + ChecksumUtil.getChecksum(dto.getSku());
            master.setRef(Long.valueOf(ref));
            master.setSlug(ref);
            master.getProductLangs().stream().forEach(x -> x.setProduct(master));
            master.setMerchantId(currentMerchantId);
        }
        else {
            if(master.getSku().equals(update.getSku())) {
                String ref = currentMerchantId.toString() + ChecksumUtil.getChecksum(dto.getSku());
                master.setSku(update.getSku());
                master.setRef(Long.valueOf(ref));
                master.setSlug(ref);
            }
            saveLang(master, update);
        }

        if(master.getChildren() == null) {
            assert(dto.getPriceObj() != null);
            master.setVariationType(VariationType.SIMPLE);
            MerchantStock stock = master.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
            stock = setMerchantStock(stock, master, dto.getPriceObj(), dto.getSalePriceObj(), dto.getCostObj(), dto.getQuantity(), dto.getAvailability(), currentMerchantId);
            if(stock.getId() == null)
                master.addMerchantStock(stock);
        }
        else { // PARENT    //if(product.getVariationType().equals(VariationType.PARENT)) {
            assert(dto.getPriceObj() == null);
            master.setVariationType(VariationType.PARENT);
            saveChildren(master, update, dto, currentMerchantId, _new);
        }

        master.setActive(false);
        master.setMerchantId(currentMerchantId);
        productRepository.save(master);

        AddProductDTO esDto = addProductMapper.toDto(master);
        if(isSaveES)
            productService.saveToElastic(esDto);
        return partnerProductMapper.toDto(master);
    }

    private void saveChildren(Product master, Product update, PartnerProduct dto, Long currentMerchantId, boolean _new) {
        Set<Product> masterChildren = master.getChildren();
        Set<Product> updateChildren = update.getChildren();


        if(_new) {
            masterChildren.stream().forEach(x -> x.variationType(VariationType.CHILD).active(true).slug(generateRef(x.getSku(), currentMerchantId)).merchantId(currentMerchantId).ref(Long.valueOf(x.getSlug())).title(master.getTitle()).parent(master).getMerchantStock().forEach(y -> y.setMerchantId(currentMerchantId)));
            return;
        }
        // Delete removed
        List<Product> remove = new ArrayList<Product>();
        for (Product c : masterChildren) {
            //product.getChildren().stream().forEach(x -> x.variationType(VariationType.SIMPLE).active(true).ref(Long.parseLong(product.getRef().toString() + i++)).setParent(product));
            Product pl = updateChildren.stream().filter(x -> x.getId() != null && x.getId().equals(c.getId())).findFirst().orElse(null);
            if (pl == null) {
                recyleImages(master, c);
                remove.add(c);
            }
        }
        masterChildren.removeAll(remove);

        for (Product c: updateChildren) {

            if (c == null || c.getId() == null) {
                String ref = currentMerchantId.toString() + String.valueOf(ChecksumUtil.getChecksum(c.getSku()));
                c.setRef(Long.valueOf(ref));
                c.setSlug(ref);
                c.setActive(true);
                c.setTitle(generateTitle(master.getTitle(), c.getVariationAttributes()));
                c.setMerchantId(currentMerchantId);
                c.getMerchantStock().stream().findFirst().get().setMerchantId(currentMerchantId);
                master.addChild(c);
                continue;
            }
            Product pl = masterChildren.stream().filter(x -> x.getId().equals(c.getId())).findFirst().orElse(null);
            ChildProduct dto2 = dto.getChildren().stream().filter(x -> x.getId().equals(c.getId())).findFirst().orElse(null);
            if(!dto2.isDirty())
                continue;
            String ref = currentMerchantId.toString() + String.valueOf(ChecksumUtil.getChecksum(c.getSku()));
            pl.setRef(Long.valueOf(ref));
            pl.setSlug(ref);
            pl.setVariationAttributes(c.getVariationAttributes());
            pl.setTitle(generateTitle(master.getTitle(), c.getVariationAttributes()));
            pl.setImage(c.getImage());
            pl.setGallery(c.getGallery());
            pl.sku(c.getSku()).image(c.getImage()).upc(c.getImage()).weight(c.getWeight()).gallery(c.getGallery());
            MerchantStock stock = pl.getMerchantStock().stream().findFirst().orElse(new MerchantStock());
            stock = setMerchantStock(stock, master, dto2.getPriceObj(), dto2.getSalePriceObj(), dto2.getCostObj(), dto2.getQuantity(), dto2.getAvailability(), currentMerchantId);
            if(stock.getId() == null)
                pl.addMerchantStock(stock);

        }
    }

    private String generateTitle(String title, List<Attribute> variationAttributes) {
        return title + variationAttributes.stream().map(Attribute::getValue).collect(Collectors.joining(", "));
    }


    private String generateRef(String sku, Long currentMerchantId) {
        return currentMerchantId.toString() + String.valueOf(ChecksumUtil.getChecksum(sku));
    }

    public void deleteImage(Long id, String image) throws ProductNotFoundException {
        final Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product " + id + " was not found in the database"));
        List<Gallery> gallery = product.getGallery();
        gallery.remove(new Gallery(image));
        productRepository.saveAndFlush(product);

        recycleService.recycleS3("s3", image);
    }

    public void recyleImages(Product parent, Product child) {
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

    private void saveLang(Product master, Product update) {
        Set<ProductLang> masterLangs = master.getProductLangs();
        Set<ProductLang> langs = update.getProductLangs();

        // Delete removed
        for (ProductLang lang: masterLangs) {
            ProductLang pl = langs.stream().filter(x -> x.getLang().equals(lang.getLang())).findFirst().orElse(null);
            if (pl == null)
                masterLangs.remove(pl);
        }

        for (ProductLang lang: langs) {
            ProductLang pl = masterLangs.stream().filter(x -> x.getLang().equals(lang.getLang())).findFirst().orElse(null);
            if (pl == null)
                master.addProductLang(lang);
            else if (!pl.equals(lang)) {
                pl.setFeatures(lang.getFeatures());
                pl.title(lang.getTitle()).description(lang.getDescription()).brand(lang.getBrand()).model(lang.getModel());
            }
        }
    }

    private MerchantStock setMerchantStock(MerchantStock stock, Product master, Price priceObj, Price salePriceObj, Price costPriceObj, BigDecimal quantity, Integer availability, Long currentMerchantId) {
        BigDecimal price = priceObj.getAmount();
        String currency = priceObj.getCurrency();

        int discount = 0;
        if(salePriceObj != null) {
            double salePrice = salePriceObj.getAmount().doubleValue();
            discount = 100 * (int)((price.doubleValue() - salePrice)/price.doubleValue());
            price = salePriceObj.getAmount();
        }
        return stock.quantity(quantity).availability(availability).cost(costPriceObj.getAmount()).allow_backorder(false)
                .price(price).discount(discount).product(master).merchantId(currentMerchantId);
    }

    public MerchantProductResponse findAllByMerchant(Long currentMerchantId, String text, Integer limit, Integer offset, Boolean active) {
        //List<AddProductDTO> result = search("tenant:"+currentTenant + " AND imported:" + imported.toString() + ((text != null)?" AND "+text:""));
        String like = "%";
        if(text != null)
            like = "%"+text+"%";

        Integer total = productRepository.countForTenantActive(currentMerchantId, active, like, VariationType.getListTypes());
        List<Product> result = productRepository.listForTenantActive(currentMerchantId, active, like, VariationType.getListTypes(), PageRequest.of((int) offset / limit, limit));
        MerchantProductResponse response = new MerchantProductResponse();
        response.setTotal(total);
        response.setHasMore((limit + offset) < total);
        response.setItems(result.stream().map(partnerProductMapper::toDto).collect(Collectors.toList()));
        return response;
    }
}
