package com.badals.shop.service.mutation;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.service.*;
import com.badals.shop.service.pojo.AddProductDTO;
import com.badals.shop.service.pojo.PartnerProductDTO;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;

import java.net.URL;
import java.time.Duration;
import java.util.List;


@Component
public class PartnerMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(PartnerMutation.class);

    private final ProductService productService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    @Value("${badals.cdnUrl}")
    private String cdnUrl;


    public PartnerMutation(ProductService productService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService) {
        this.productService = productService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
    }


    public Message createPartnerProduct(PartnerProductDTO productDTO) {
        return null;
    }
    public PresignedUrl getPartnerImageUploadUrl(String filename, String contentType) {
        return null; //PresignedUrl
    }

/*    public Message addPrice(price: PriceInput)  {
        return null;
    }
    public Message addLangFields(desc: ProductDescriptionInput)  {
        return null;
    }
    public Message addVariation(variation: VariationProductInput) {
        return null;
    }

    public Message removePrice(price: PriceInput)  {
        return null;
    }
    public Message removeLangFields(desc: ProductDescriptionInput)  {
        return null;
    }
    public Message removeVariation(variation: VariationProductInput) {
        return null;
    }

    public Message approveProduct(id: ID) {
        return null;
    }*/

}

