package com.badals.shop.service.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.Order;
import com.badals.shop.domain.Payment;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.checkout.helper.Message;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.enumeration.OverrideType;
import com.badals.shop.domain.pojo.Attribute;
import com.badals.shop.service.*;
import com.badals.shop.service.dto.*;
import com.badals.shop.web.rest.errors.ProductNotFoundException;
import com.badals.shop.xtra.amazon.NoOfferException;
import com.badals.shop.xtra.amazon.Pas5Service;
import com.badals.shop.xtra.amazon.PricingException;
import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class AdminMutation implements GraphQLMutationResolver {
    @Autowired
    private ProductService productService;

    @Autowired
    private Pas5Service pasService;

    @Autowired
    private ProductLangService productLangService;

    @Autowired
    private PricingRequestService pricingRequestService;

    @Autowired
    MessageSource messageSource;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductOverrideService productOverrideService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private MailService mailService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message contact(final Long id) {
        orderService.sendPaymnetMessage(id);
        return new Message("SMS Sent successfully");
    }

    public PurchaseDTO createOrUpdatePurchase(PurchaseDTO dto, List<PurchaseItemDTO> items) {
        PurchaseDTO purchase = purchaseService.createOrUpdatePurchase(dto, items);
        return null;
    }

    public ProductDTO createOverride(String sku, OverrideType type, String override, Boolean active, Boolean lazy) throws PricingException, NoOfferException, ProductNotFoundException {
        ProductOverrideDTO dto = new ProductOverrideDTO(sku, type, override, active, lazy);
        productOverrideService.saveOrUpdate(dto);
        return productService.lookupPas(sku, false, true);
    }

    public Message completePricingRequest(Long id) {
        pricingRequestService.complete(id);
        return new Message("done");
    }

    public Message sendPaymentSms(Long id, String mobile) throws Exception {
        orderService.sendRequestPaymentSms(id, mobile);
        return new Message("done");
    }

    public OrderDTO discountOrder(Long id){
        return null;
    }

    public OrderDTO setOrderState(Long id, OrderState state){
        return null;
    }

    public OrderDTO cancelOrder(Long id){
        return null;
    }

    public Message sendOrderLevelEmail(Long id, String template){

        if(template.equalsIgnoreCase("NEW_ORDER")) {
            OrderDTO order = orderService.getOrderWithOrderItems(id).orElse(null);
            CustomerDTO customer = order.getCustomer();
            mailService.sendOrderCreationMail(customer, order);
        }
        else if(template.equalsIgnoreCase("NEW_PAYMENT")) {
            PaymentDTO payment = paymentService.findOne(id).orElse(null);
            OrderDTO order = orderService.getOrderWithOrderItems(payment.getOrderId()).orElse(null);
            CustomerDTO customer = order.getCustomer();
            mailService.sendPaymentAddedMail(customer, payment);
        }
        return new Message("Success");
    }
    public Message sendProductLevelEmail(Long orderId, ArrayList<Long> orderItems, String template){
        return null;
    }

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
}

