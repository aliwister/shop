package com.badals.shop.graph.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.repository.CheckoutRepository;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProductOverrideDTO;
import com.badals.shop.service.dto.SpeedDialDTO;
import com.badals.shop.service.util.MailService;
import com.badals.shop.web.rest.errors.ProductNotFoundException;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class PricingMutation implements GraphQLMutationResolver {
    private final PricingRequestService pricingRequestService;
    private final PurchaseService purchaseService;
    private final MailService mailService;
    private final CustomerService customerService;
    private final ProductOverrideService productOverrideService;
    private final AwsService awsService;
    private final MessageSource messageSource;
    private final CheckoutRepository checkoutRepository;

    public PricingMutation( PricingRequestService pricingRequestService, MessageSource messageSource, CustomerService customerService, UserService userService, ProductOverrideService productOverrideService, PurchaseService purchaseService,  MailService mailService, AwsService awsService, CheckoutRepository checkoutRepository) {
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.customerService = customerService;
        this.productOverrideService = productOverrideService;
        this.purchaseService = purchaseService;
        this.mailService = mailService;
        this.awsService = awsService;
        this.checkoutRepository = checkoutRepository;
    }


    @PreAuthorize("hasRole('ROLE_USER')")
    public Message addToPricingQ(String asin) {
        Customer loginUser = customerService.getUserWithAuthorities().orElse(null);
        if(loginUser == null)
            return new Message("You have to be logged in to request a price");

        try {
            pricingRequestService.push(asin, loginUser.getEmail());
        }
        catch(RuntimeException e) {
            return new Message(messageSource.getMessage("pricing.request.exists", null, LocaleContextHolder.getLocale()));
        }
        return new Message(messageSource.getMessage("pricing.request.success", null, LocaleContextHolder.getLocale()));
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO createOverride(String sku, OverrideType type, String override, Boolean active, Boolean lazy, int merchantId, boolean submitOnly, String dial) throws ProductNotFoundException {
        if(override != null && !override.trim().isEmpty()) {
            ProductOverrideDTO dto = new ProductOverrideDTO(sku, type, override, active, lazy);
            productOverrideService.saveOrUpdate(dto);
        }


        if(submitOnly)
            return null;
        ProductDTO productDTO = null;
/*        if(merchantId == 1L)
            productDTO = productService.lookupForcePas(sku, false, false, true);
        else if(merchantId == -1L)
            productDTO = productService.lookupForcePas(sku, false, false, false);
        else if(merchantId == 0L)
            productDTO = productService.lookupPas(sku, true, false);
        else *//*if(merchantId == 2L)
            productDTO = productService.lookupEbay(sku);*/
        return productDTO;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message completePricingRequestAndEmail(Long id) throws ProductNotFoundException {
        PricingRequestDTO dto = pricingRequestService.complete(id);
        // Get all overrides for this customer
        // Send email
        // Update overrides
        List<PricingRequestDTO> dtos = pricingRequestService.findAllByCreatedByAndDone(dto.getEmail(), true);
        Customer customer = customerService.findByEmail(dto.getEmail());
        mailService.sendPricingMail(customer, dtos);
        pricingRequestService.setEmailSent(dtos);
        return new Message("done");
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message completePricingRequest(Long id) {
        pricingRequestService.complete(id);
        return new Message("done");
    }


}
