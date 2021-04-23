package com.badals.shop.graph.mutation;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.service.*;
import com.badals.shop.service.pojo.PartnerProduct;
import com.badals.shop.service.pojo.ProductEnvelope;
import com.badals.shop.service.util.ChecksumUtil;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.LocalDate;


@Component
public class PartnerMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(PartnerMutation.class);

    private final PartnerService partnerService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    private final AwsService awsService;

    @Value("${badals.cdnUrl}")
    private String cdnUrl;


    public PartnerMutation(PartnerService partnerService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService, AwsService awsService) {
        this.partnerService = partnerService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
        this.awsService = awsService;
    }

    public ProductEnvelope savePartnerProduct(PartnerProduct product) throws ProductNotFoundException {
        String t =  TenantContext.getCurrentTenant();
        log.info("Tenant: " + t);
        Long mId = 1L;//TenantContext.getCurrentMerchantId();
        Long tId = TenantContext.getCurrentTenantId();
        String merchant = "Badals.com";//TenantContext.getCurrentMerchant();
        String tenant = TenantContext.getCurrentTenant();



        PartnerProduct p = null;
        StringBuilder message = new StringBuilder();
        Integer code = 202;

        try {
            p = partnerService.savePartnerProduct(product, mId, true);
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

    public PresignedUrl getPartnerImageUploadUrl(String filename, String contentType) {
        String t =  "badals";//TenantContext.getCurrentTenant();
        String m = "badals";//TenantContext.getCurrentMerchant();
        String fileKey = ChecksumUtil.getChecksum(filename + LocalDate.now())+filename.substring(filename.length() - 4);
        String objectKey = "_m/" + m + "/" + fileKey;

        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), cdnUrl + "/" + m + "/" + fileKey,m+"/"+fileKey, "200");
    }

    public Message approveProduct(Long id) {
        return null;
    }
    public Message setOrderState(OrderState value) {
        return null;
    }
    public Message deleteProduct(Long id) {
        partnerService.deleteProduct(id);
        return new Message("ok");
    }

}

