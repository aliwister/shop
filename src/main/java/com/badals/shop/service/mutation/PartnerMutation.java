package com.badals.shop.service.mutation;

import com.badals.shop.aop.logging.TenantContext;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.domain.pojo.Price;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.ProductLangDTO;
import com.badals.shop.service.pojo.ChildProduct;
import com.badals.shop.service.pojo.PartnerProduct;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;


@Component
public class PartnerMutation implements GraphQLMutationResolver {
    private final Logger log = LoggerFactory.getLogger(PartnerMutation.class);

    private final PartnerService partnerService;

    private final ProductLangService productLangService;

    private final PricingRequestService pricingRequestService;

    private final MessageSource messageSource;

    private final UserService userService;

    @Value("${badals.cdnUrl}")
    private String cdnUrl;


    public PartnerMutation(PartnerService partnerService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, UserService userService) {
        this.partnerService = partnerService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    public Message createPartnerProduct(PartnerProduct product) {
        String t =  TenantContext.getCurrentTenant();
        log.info("Tenant: " + t);
        Long mId = 1L;//TenantContext.getCurrentMerchantId();
        Long tId = TenantContext.getCurrentTenantId();
        String merchant = "Badals.com";//TenantContext.getCurrentMerchant();
        String tenant = TenantContext.getCurrentTenant();

        partnerService.createPartnerProduct(product, mId, merchant, tId, tenant, true);
        return new Message("Okay");
    }

    public PresignedUrl getPartnerImageUploadUrl(String filename, String contentType) {
        return null; //PresignedUrl
    }

    public Message savePrice(Long id, Price price)  {
        partnerService.savePrice(id, price);
        return new Message("Okay");
    }
    public Message saveLang(Long id, ProductLangDTO lang)  {
        partnerService.saveLang(id, lang);
        return new Message("Okay");
    }
    public Message saveChild(Long id, ChildProduct child) {
        partnerService.saveChild(id, child);
        return new Message("Okay");
    }

    public Message removePrice(Price price)  {
        return null;
    }
    public Message removeLang(String lang)  {
        return null;
    }
    public Message removeChild(Long id) {
        return null;
    }

    public Message approveProduct(Long id) {
        return null;
    }

}

