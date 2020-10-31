package com.badals.shop.service.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.checkout.CheckoutCart;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.checkout.helper.PresignedUrl;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.repository.CheckoutCartRepository;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.*;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.net.URL;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminMutation implements GraphQLMutationResolver {
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

    public AdminMutation(ProductService productService, Pas5Service pasService, ProductLangService productLangService, PricingRequestService pricingRequestService, MessageSource messageSource, CustomerService customerService, UserService userService, OrderService orderService, ProductOverrideService productOverrideService, PurchaseService purchaseService, PaymentService paymentService, MailService mailService, AwsService awsService, SpeedDialService speedDialService, CheckoutCartRepository checkoutCartRepository) {
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
    public Message contact(final Long id) {
        orderService.sendPaymnetMessage(id);
        return new Message("SMS Sent successfully");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO createPurchase(PurchaseDTO dto) {
        PurchaseDTO purchase = purchaseService.save(dto);
        return purchase;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO updatePurchase(PurchaseDTO dto, List<PurchaseItemDTO> items) {
        PurchaseDTO purchase = purchaseService.updatePurchase(dto, items);
        return purchase;
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
        if(merchantId == 1L)
            productDTO = productService.lookupForcePas(sku, false, false, true);
        else if(merchantId == -1L)
            productDTO = productService.lookupForcePas(sku, false, false, false);
        else if(merchantId == 0L)
            productDTO = productService.lookupPas(sku, true, false);
        else if(merchantId == 2L)
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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendPaymentSms(Long id, String mobile) throws Exception {
        orderService.sendRequestPaymentSms(id, mobile);
        return new Message("done");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO discountOrder(Long id){
        return null;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public OrderDTO setOrderState(Long id, OrderState state){
        return orderService.setStatus(id, state);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PurchaseDTO setPurchaseState(Long id, OrderState state){
        return purchaseService.setStatus(id, state);
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendOrderLevelEmail(Long id, String template) {

        if(template.equalsIgnoreCase("NEW_ORDER")) {
            orderService.sendConfirmationEmail(id);
        }
        else if(template.equalsIgnoreCase("NEW_PAYMENT")) {
            PaymentDTO payment = paymentService.findOne(id).orElse(null);
            OrderDTO order = orderService.getOrderWithOrderItems(payment.getOrderId()).orElse(null);
            CustomerDTO customer = order.getCustomer();
            mailService.sendPaymentAddedMail(customer, payment);
        }

        return new Message("Success");
    }


    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message sendProductLevelEmail(Long orderId, ArrayList<Long> orderItems, String template) {
        if (template.equalsIgnoreCase("VOLTAGE")) {
            orderService.sendVoltageEmail(orderId, orderItems);
        }
        return new Message("Success");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PaymentDTO addPayment(Long orderId, BigDecimal amount, String paymentMethod, String authCode) {
        PaymentDTO payment = paymentService.addPayment(orderId, amount, paymentMethod, authCode);
        OrderDTO order = orderService.setOrderState(orderId, OrderState.PAYMENT_ACCEPTED);
        // Send Email
        payment = paymentService.findOne(payment.getId()).orElse(null);
        CustomerDTO customer = order.getCustomer();
        mailService.sendPaymentAddedMail(customer, payment);
        // Return
        return payment;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public PresignedUrl getUploadUrl(String filename, String contentType) {
        String objectKey = filename;
        URL url = awsService.presignPutUrl(objectKey, contentType);
        return new PresignedUrl(url.toString(), "https://cdn.badals.com/" + filename, "200");
    }

}

