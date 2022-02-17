package com.badals.shop.graph.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.service.pojo.Message;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.repository.CheckoutCartRepository;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.dto.ProductDTO;
import com.badals.shop.service.dto.ProductOverrideDTO;
import com.badals.shop.service.dto.SpeedDialDTO;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

@Component
public class PricingMutation implements GraphQLMutationResolver {
    private final ProductService productService;
    private final Pas5Service pasService;
    private final ProductLangService productLangService;
    private final PricingRequestService pricingRequestService;
    private final UserService userService;
    private final OrderService orderService;
    private final PurchaseService purchaseService;
    private final PaymentService paymentService;
    private final MailService mailService;
    private final CustomerService customerService;
    private final ProductOverrideService productOverrideService;
    private final AwsService awsService;
    private final SpeedDialService speedDialService;

    private final MessageSource messageSource;
    private final CheckoutCartRepository checkoutCartRepository;

    public PricingMutation(ProductService productService, Pas5Service pasService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, CustomerService customerService, UserService userService, OrderService orderService, ProductOverrideService productOverrideService, PurchaseService purchaseService, PaymentService paymentService, MailService mailService, AwsService awsService, SpeedDialService speedDialService, CheckoutCartRepository checkoutCartRepository) {
        this.productService = productService;
        this.pasService = pasService;
        this.productLangService = productLangService;
        this.pricingRequestService = pricingRequestService;
        this.messageSource = messageSource;
        this.customerService = customerService;
        this.userService = userService;
        this.orderService = orderService;
        this.productOverrideService = productOverrideService;
        this.purchaseService = purchaseService;
        this.paymentService = paymentService;
        this.mailService = mailService;
        this.awsService = awsService;
        this.speedDialService = speedDialService;
        this.checkoutCartRepository = checkoutCartRepository;
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ProductDTO createOverride(String sku, OverrideType type, String override, Boolean active, Boolean lazy, int merchantId, boolean submitOnly, String dial) throws PricingException, NoOfferException, ProductNotFoundException {
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
        else */if(merchantId == 2L)
            productDTO = productService.lookupEbay(sku);


        if(dial != null && !dial.trim().isEmpty()) {
            speedDialService.save(new SpeedDialDTO().dial(dial).ref(productDTO.getRef()).expires(Instant.now()));
        }


        return productDTO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message completePricingRequestAndEmail(Long id) throws PricingException, NoOfferException, ProductNotFoundException {
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message completePricingRequest(Long id) {
        pricingRequestService.complete(id);
        return new Message("done");
    }


}

