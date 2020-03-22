package com.badals.shop.service.query;


import com.badals.shop.domain.PricingRequest;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AdminQuery extends ShopQuery implements GraphQLQueryResolver {

    @Autowired
    PricingRequestService pricingRequestService;

    public List<PricingRequestDTO> pricingRequests() {
        return pricingRequestService.findUnprocessed();
    }
}
