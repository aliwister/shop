package com.badals.shop.service.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.HashtagDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProductLangDTO;

import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;


@Component
public class ProductMutation implements GraphQLMutationResolver {
    private final ProductService productService;

    private final Pas5Service pasService;

    private final HashtagService hashtagService;

    private final SpeedDialService speedDialService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;


    public ProductMutation(ProductService productService, Pas5Service pasService, HashtagService hashtagService, SpeedDialService speedDialService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService) {
        this.productService = productService;
        this.pasService = pasService;
        this.hashtagService = hashtagService;
        this.speedDialService = speedDialService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    public ProductDTO createProduct(final Long ref, final Long parent, final String sku, final String upc, final LocalDate releaseDate) {
        return this.productService.createProduct(ref, parent, sku, upc, releaseDate);
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO createNewProduct(final ProductDTO product) {
        return this.productService.createNewProduct(product);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Attribute indexProduct(final long id) {
        return this.productService.indexProduct(id);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductLangDTO addI18n(Long id, final ProductLangDTO productI18n) {
        return this.productLangService.addI18n(id, productI18n);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO pasLookup(String asin) throws ProductNotFoundException, PricingException, NoOfferException {
        return this.productService.lookupPas(asin, false, false);
    }

    //@PreAuthorize("isAuthenticated()")
    @PreAuthorize("hasRole('ROLE_USER')")
    public Message addToPricingQ(String asin) {
        Customer loginUser = userService.getUserWithAuthorities().orElse(null);
        if(loginUser == null)
            return new Message("You have to be logged in to request a price");

        try {
            pricingRequestService.push(asin, loginUser.getEmail());
        }
        catch(PricingException e) {
            return new Message(messageSource.getMessage("pricing.request.exists", null, LocaleContextHolder.getLocale()));
        }
        return new Message(messageSource.getMessage("pricing.request.success", null, LocaleContextHolder.getLocale()));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO approveProduct(Long id){
        return null;
    }

    public Message addToElastic( Long id, String  sku, String  name, String  name_ar, List<String> shops) {
        productService.addToElastic(id, sku, name, name_ar, shops);
        return new Message("success");
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message createHashtag(HashtagDTO hash){
        hashtagService.save(hash);
        return new Message("Done");
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message setHashtags(List<String> hashs, Long ref) throws ProductNotFoundException {
        productService.setHashtags(hashs, ref);
        return new Message("Done");
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message setDial(String dial, Long ref){
        speedDialService.addDial(dial, ref);
        return new Message("Done");
    }
}

