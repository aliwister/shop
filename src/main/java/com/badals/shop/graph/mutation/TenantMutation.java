package com.badals.shop.graph.mutation;

import com.badals.shop.aop.logging.TenantContext;
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
import com.badals.shop.service.TenantProductService;
import com.badals.shop.service.util.ChecksumUtil;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;


@Component
public class TenantMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(TenantMutation.class);

    private final TenantProductService productService;
    private final TenantCartService cartService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final AwsService awsService;

    @Value("${badals.cdnUrl}")
    private String cdnUrl;


    public TenantMutation(TenantProductService productService, TenantCartService cartService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService, AwsService awsService) {
        this.productService = productService;
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

    @PreAuthorize("hasRole('ROLE_MERCHANT')")
    public PresignedUrl getPartnerImageUploadUrl(String filename, String contentType) {
        String t = TenantContext.getCurrentTenant();
        String m = TenantContext.getCurrentMerchant();
        String fileKey = ChecksumUtil.getChecksum(filename + LocalDate.now())+filename.substring(filename.length() - 4);
        String objectKey = "_m/" + m + "/" + fileKey;

        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), cdnUrl + "/" + m + "/" + fileKey,m+"/"+fileKey, "200");
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
    public CartResponse updateTenantCart(final String secureKey, final List<CartItemDTO> items, boolean isMerge, String _locale) {
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

