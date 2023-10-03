package com.badals.shop.graph.query;

import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PricingQuery extends BaseQuery implements GraphQLQueryResolver {

    final private PricingRequestService pricingRequestService;

    public PricingQuery(PricingRequestService pricingRequestService) {
        this.pricingRequestService = pricingRequestService;
    }

    //@PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<PricingRequestDTO> pricingRequests() {
        return pricingRequestService.findUnprocessed();
    }
}
