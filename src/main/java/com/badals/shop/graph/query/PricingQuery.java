package com.badals.shop.graph.query;


import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricingQuery extends BaseQuery implements GraphQLQueryResolver {

    @Autowired
    private PricingRequestService pricingRequestService;

    @Autowired
    private ProductService productService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PricingRequestDTO> pricingRequests() {
        return pricingRequestService.findUnprocessed();
    }

    public String parentOf(String sku) {
        return productService.getParentOf(sku);
    }
}
