package com.badals.shop.graph.mutation;

import com.badals.shop.aop.tenant.TenantContext;
import com.badals.shop.domain.enumeration.AssetType;
import com.badals.shop.domain.tenant.S3UploadRequest;
import com.badals.shop.domain.tenant.TenantHashtag;
import com.badals.shop.service.dto.ProfileHashtagDTO;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.service.pojo.PresignedUrl;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.graph.CartResponse;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.CartDTO;
import com.badals.shop.service.dto.CartItemDTO;
import com.badals.shop.service.pojo.CheckoutSession;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.pojo.ProductEnvelope;
import com.badals.shop.service.TenantCartService;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;
import java.util.Locale;


@Component
public class TenantMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(TenantMutation.class);

    private final TenantAdminProductService productService;
    private final TenantSetupService tenantSetupService;
    private final TenantCartService cartService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final AwsService awsService;

    @Value("${profileshop.cdnUrl}")
    private String cdnUrl;


    public TenantMutation(TenantAdminProductService productService, TenantSetupService tenantSetupService, TenantCartService cartService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService, AwsService awsService) {
        this.productService = productService;
        this.tenantSetupService = tenantSetupService;
        this.cartService = cartService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.awsService = awsService;
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public ProductEnvelope savePartnerProduct(PartnerProduct product) throws ProductNotFoundException {
        PartnerProduct p = null;
        StringBuilder message = new StringBuilder();
        Integer code = 202;

        try {
            p = productService.savePartnerProduct(product, true);
            message.append("Success");
        }
        catch(Throwable e) {
            e.printStackTrace();
            while (e != null) {
                message.append(e.getMessage());
                e = e.getCause();
            }

            code = 400;
        }
        log.error(message.toString());
        return new ProductEnvelope(p, message.toString(), code);
    }

    //@PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PresignedUrl getPartnerImageUploadUrl(String filename, String contentType, AssetType assetType) {
        return productService.getS3UploadUrl(filename, contentType, assetType);
    }

    public Message completeUpload(Long fileHandle) {
        S3UploadRequest request = productService.findUploadRequest(fileHandle);
        if (request.getAssetType() == AssetType.LOGO)
            tenantSetupService.updateLogo(request);

        tenantSetupService.addMedia(request);

        return new Message("success", "200");
    }

    public ProfileHashtagDTO saveTenantTag (ProfileHashtagDTO hashtag) {
        return productService.saveTag(hashtag);
    }

    public Message setSliderList(String locale, List<String> images) {
        tenantSetupService.setSliders(locale, images);
        return new Message("success");
    }



    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Message publishProduct(Long id) throws ProductNotFoundException {
        productService.setProductPublished(id, true);
        return new Message("Product published successfully");
    }
    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Message unpublishProduct(Long id) throws ProductNotFoundException {
        productService.setProductPublished(id, false);
        return new Message("Product set to draft successfully");
    }
    public Message setOrderState(OrderState value) {
        return null;
    }

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public Message deleteProduct(Long id) throws ProductNotFoundException {
        productService.deleteProduct(id);
        return new Message("ok");
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CartResponse updateTenantCart(final String secureKey, final List<CartItemDTO> items, boolean isMerge) {
        Locale l = LocaleContextHolder.getLocale();
        CartDTO cart = this.cartService.updateCart(secureKey, items, isMerge);
        CartResponse response = new CartResponse();
        response.setCart(cart);
        response.setSuccess(true);
        response.setMessage("Cart Saved Successfully");
        return response;
    }

    //@PreAuthorize("hasRole('ROLE_USER')")
    public CheckoutSession createTenantCheckout(final String secureKey, final List<CartItemDTO> items) {
        String token = cartService.createCheckout(secureKey, items);
        return new CheckoutSession("/checkout/" + token + "/address", token);
    }
}

