package com.badals.shop.service.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.ProductLangService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.UserService;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProductLangDTO;

import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.S3ClientBuilder;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;


@Component
public class ProductMutation implements GraphQLMutationResolver {
    private final ProductService productService;

    private final Pas5Service pasService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;


    public ProductMutation(ProductService productService, Pas5Service pasService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService) {
        this.productService = productService;
        this.pasService = pasService;
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



}

