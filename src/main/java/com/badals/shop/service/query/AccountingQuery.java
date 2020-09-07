package com.badals.shop.service.query;


import com.badals.shop.domain.Payment;
import com.badals.shop.domain.enumeration.OrderState;
import com.badals.shop.domain.pojo.PaymentResponse;
import com.badals.shop.service.PricingRequestService;
import com.badals.shop.service.ProductService;
import com.badals.shop.service.dto.PaymentDTO;
import com.badals.shop.service.dto.PricingRequestDTO;
import com.coxautodev.graphql.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AccountingQuery extends ShopQuery implements GraphQLQueryResolver {

/*    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    public List<PaymentDTO> payments(Integer limit) {
        return null;
    }*/
    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    public PaymentResponse payments(List<OrderState> state, Integer offset, Integer limit, String searchText){
        return null;
    }
    @PreAuthorize("hasRole('ROLE_ACCOUNTANT')")
    public PaymentDTO payment(Long id) {
        return null;
    }
}
