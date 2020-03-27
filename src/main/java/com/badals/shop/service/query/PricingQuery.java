package com.badals.shop.service.query;


import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.repository.projection.PurchaseQueue;
import com.badals.shop.service.OrderService;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.PurchaseService;
import com.badals.shop.service.dto.OrderDTO;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.badals.shop.service.dto.PurchaseDTO;
import com.badals.shop.web.rest.errors.OrderNotFoundException;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricingQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    private PricingRequestService pricingRequestService;

    @Autowired
    private ProductService productService;

    public List<PricingRequestDTO> pricingRequests() {
        return pricingRequestService.findUnprocessed();
    }

    public String parentOf(String sku) {
        return productService.getParentOf(sku);
    }
}
