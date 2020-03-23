package com.badals.shop.service.mutation;

import com.badals.shop.domain.Customer;
import com.badals.shop.domain.ProductOverride;
import com.badals.shop.domain.checkout.helper.Message;
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

import java.time.LocalDate;
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


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Message contact(final Long id) {
        orderService.sendPaymnetMessage(id);
        return new Message("SMS Sent successfully");
    }

    public PurchaseDTO createPurchase(List<PurchaseItemDTO> items) {
        return null;
    }

    public ProductDTO createOverride(String sku, OverrideType type, String override, Boolean active, Boolean lazy) throws PricingException, NoOfferException, ProductNotFoundException {
        ProductOverrideDTO dto = new ProductOverrideDTO(sku, type, override, active, lazy);
        productOverrideService.saveOrUpdate(dto);
        return productService.lookupPas(sku, true, false);
    }

    public Message completePricingRequest(Long id) {
        pricingRequestService.complete(id);
        return new Message("done");
    }
}

